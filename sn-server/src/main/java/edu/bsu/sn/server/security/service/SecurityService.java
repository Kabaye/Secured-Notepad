package edu.bsu.sn.server.security.service;

import edu.bsu.sn.server.notepad.model.event.UserLoggedIn;
import edu.bsu.sn.server.security.ks.CustomKeyStore;
import edu.bsu.sn.server.security.model.AESKeyAndIvSpec;
import edu.bsu.sn.server.security.model.LogInUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final KeyFactory keyFactoryRSA;
    private final CustomKeyStore keyStore;
    private final KeyGenerator keyGeneratorAES;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SecureRandom secureRandom = new SecureRandom();

    private static final String CIPHER_USER_ID_FORMAT = "user-%s-cipher";

    @SneakyThrows
    public AESKeyAndIvSpec logInAndGetSecretKey(LogInUser logInUser) {
        EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(logInUser.getPublicKey());
        PublicKey publicKey = keyFactoryRSA.generatePublic(encodedKeySpec);

        SecretKey sessionKey = keyGeneratorAES.generateKey();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(sessionKey.getEncoded());

        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);

        final Cipher cipherAES = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherAES.init(Cipher.ENCRYPT_MODE, sessionKey, new IvParameterSpec(iv));

        keyStore.getUsersCiphers().put(CIPHER_USER_ID_FORMAT.formatted(logInUser.getUsername()), cipherAES);

        applicationEventPublisher.publishEvent(new UserLoggedIn().setName(logInUser.getUsername()));
        return new AESKeyAndIvSpec()
                .setAesKey(result)
                .setIvSpec(iv);
    }

    @SneakyThrows
    public String secureText(String incomingText, String username) {
        return Base64.getEncoder().encodeToString(keyStore.getUsersCiphers()
                .get(CIPHER_USER_ID_FORMAT.formatted(username))
                .doFinal(incomingText.getBytes()));
    }
}
