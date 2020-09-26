package edu.bsu.sn.server.security.service;

import edu.bsu.sn.server.notepad.model.event.NewUserEvent;
import edu.bsu.sn.server.security.ks.CustomKeyStore;
import edu.bsu.sn.server.security.model.NewUser;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final KeyFactory keyFactoryRSA;
    private final CustomKeyStore keyStore;
    private final KeyGenerator keyGeneratorAES;
    private final ApplicationEventPublisher applicationEventPublisher;

    @SneakyThrows
    public byte[] registerNewUser(NewUser newUser) {
        EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(newUser.getPublicKey());
        PublicKey publicKey = keyFactoryRSA.generatePublic(encodedKeySpec);
        String userId = "user-" + newUser.getLogin() + "-public-key";
        keyStore.getUsersKeys().put(userId, publicKey);

        SecretKey sessionKey = keyGeneratorAES.generateKey();
        keyStore.getKeyStore().setEntry(userId, new KeyStore.SecretKeyEntry(sessionKey),
                new KeyStore.PasswordProtection(keyStore.getSecurityProperties().getPassword().toCharArray()));

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(sessionKey.getEncoded());

        applicationEventPublisher.publishEvent(new NewUserEvent().setName(newUser.getLogin()));

        return result;
    }
}
