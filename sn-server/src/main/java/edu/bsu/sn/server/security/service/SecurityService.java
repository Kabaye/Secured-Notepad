package edu.bsu.sn.server.security.service;

import edu.bsu.sn.server.notepad.model.event.NewUserEvent;
import edu.bsu.sn.server.security.ks.CustomKeyStore;
import edu.bsu.sn.server.security.model.LogInUser;
import edu.bsu.sn.server.security.model.SessionKeyAndUser;
import edu.bsu.sn.server.security.model.SessionKeyRequest;
import edu.bsu.sn.server.security.ps.CustomPasswordStore;
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
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final KeyFactory keyFactoryRSA;
    private final CustomKeyStore keyStore;
    private final KeyGenerator keyGeneratorAES;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SecureRandom secureRandom = new SecureRandom();
    private final CustomPasswordStore passwordStore;

    private static final String CIPHER_USER_ID_FORMAT = "user-%s-cipher";

    @SneakyThrows
    public SessionKeyAndUser getSessionKey(SessionKeyRequest sessionKeyRequest) {
        EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(sessionKeyRequest.getPublicKey());
        PublicKey publicKey = keyFactoryRSA.generatePublic(encodedKeySpec);

        SecretKey sessionKey = keyGeneratorAES.generateKey();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(sessionKey.getEncoded());

        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        final IvParameterSpec params = new IvParameterSpec(iv);

        final Cipher cipherAESEncryption = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherAESEncryption.init(Cipher.ENCRYPT_MODE, sessionKey, params);

        final Cipher cipherAESDecryption = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherAESDecryption.init(Cipher.DECRYPT_MODE, sessionKey, params);

        final Instant expiration = Instant.now().plus(Duration.ofHours(2));

        keyStore.getUsersCiphers().put(CIPHER_USER_ID_FORMAT.formatted(sessionKeyRequest.getUsername()),
                Map.entry(expiration, Map.entry(cipherAESEncryption, cipherAESDecryption)));

        byte[] passwordEncrypted = null;
        if (Objects.isNull(passwordStore.getPassword(sessionKeyRequest.getUsername()))) {
            byte[] password = passwordStore.generateRandomPassword();
            passwordStore.addPassword(sessionKeyRequest.getUsername(), password);

            passwordEncrypted = cipherAESEncryption.doFinal(password);

            applicationEventPublisher.publishEvent(new NewUserEvent().setName(sessionKeyRequest.getUsername()));
        }

        return new SessionKeyAndUser()
                .setAesKey(result)
                .setIvSpec(iv)
                .setPassword(passwordEncrypted)
                .setExpiresIn(expiration);
    }

    @SneakyThrows
    public boolean login(LogInUser logInUser) {
        final byte[] password = keyStore.getUsersCiphers()
                .get(CIPHER_USER_ID_FORMAT.formatted(logInUser.getUsername()))
                .getValue().getValue()
                .doFinal(logInUser.getPassword());
        final boolean loggedIn = passwordStore.checkPassword(logInUser.getUsername(),
                password);
        if (!loggedIn) {
            keyStore.getUsersCiphers().remove(logInUser.getUsername());
        }
        return loggedIn;
    }

    @SneakyThrows
    public String encryptText(String text, String username) {
        if (Instant.now().isBefore(keyStore.getUsersCiphers()
                .get(CIPHER_USER_ID_FORMAT.formatted(username)).getKey())) {
            return Base64.getEncoder().encodeToString(keyStore.getUsersCiphers()
                    .get(CIPHER_USER_ID_FORMAT.formatted(username))
                    .getValue().getKey()
                    .doFinal(text.getBytes()));
        }
        throw new RuntimeException("Session key is expired! Please, login again.");
    }

    @SneakyThrows
    public String decryptText(String text, String username) {
        if (Instant.now().isBefore(keyStore.getUsersCiphers()
                .get(CIPHER_USER_ID_FORMAT.formatted(username)).getKey())) {
            return new String(keyStore.getUsersCiphers()
                    .get(CIPHER_USER_ID_FORMAT.formatted(username))
                    .getValue().getValue()
                    .doFinal(Base64.getDecoder().decode(text)));
        }
        throw new RuntimeException("Session key is expired! Please, login again.");
    }
}
