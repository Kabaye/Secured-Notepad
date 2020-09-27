package edu.bsu.sn.client.security.ks;

import edu.bsu.sn.client.security.properties.SecurityProperties;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

@Getter
@Component
public class CustomKeyStore {
    private final Cipher cipherAES;
    private final Cipher cipherRSA;
    private final PublicKey publicKey;
    private final SecurityProperties securityProperties;

    @SneakyThrows
    public CustomKeyStore(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        cipherRSA = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipherRSA.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

        cipherAES = Cipher.getInstance("AES/CBC/PKCS5Padding");

        publicKey = keyPair.getPublic();
    }
}
