package edu.bsu.sn.server.security.service;

import edu.bsu.sn.server.notepad.model.event.UserLoggedIn;
import edu.bsu.sn.server.security.ks.CustomKeyStore;
import edu.bsu.sn.server.security.model.LogInUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final KeyFactory keyFactoryRSA;
    private final CustomKeyStore keyStore;
    private final KeyGenerator keyGeneratorAES;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final String SESSION_KEY_USER_ID_FORMAT = "user-%s-session-key";
    private static final String CIPHER_USER_ID_FORMAT = "user-%s-cipher";

    @SneakyThrows
    public byte[] logInAndGetSecretKey(LogInUser logInUser) {
        EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(logInUser.getPublicKey());
        PublicKey publicKey = keyFactoryRSA.generatePublic(encodedKeySpec);

        SecretKey sessionKey = keyGeneratorAES.generateKey();
        keyStore.getKeyStore().setEntry(SESSION_KEY_USER_ID_FORMAT.formatted(logInUser.getLogin()), new KeyStore.SecretKeyEntry(sessionKey),
                new KeyStore.PasswordProtection(keyStore.getSecurityProperties().getPassword().toCharArray()));

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(sessionKey.getEncoded());

        applicationEventPublisher.publishEvent(new UserLoggedIn().setName(logInUser.getLogin()));
        return result;
    }

    @SneakyThrows
    public String secureText(String incomingText, String username) {
        Key sessionKey = keyStore.getKeyStore().getKey(SESSION_KEY_USER_ID_FORMAT.formatted(username),
                keyStore.getSecurityProperties().getPassword().toCharArray());

        byte[] encrypted =  Objects.requireNonNull(keyStore.getUsersCiphers()
                .computeIfAbsent(CIPHER_USER_ID_FORMAT.formatted(username), s -> {
                    try {
                        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
                        return cipher;
                    } catch (Exception ignored) {
                    }
                    return null;
                })).doFinal(incomingText.getBytes());

        return new String(encrypted);
    }
}
