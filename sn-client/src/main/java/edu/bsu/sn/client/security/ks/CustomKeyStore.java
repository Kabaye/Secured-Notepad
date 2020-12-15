package edu.bsu.sn.client.security.ks;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import javax.crypto.Cipher;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CustomKeyStore {
    private final Cipher cipherSerpentDecryption;
    private final Cipher cipherSerpentEncryption;
    private final Cipher cipherRSA;
    private final PublicKey publicKey;

    @SneakyThrows
    public CustomKeyStore() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        cipherRSA = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipherRSA.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

        cipherSerpentDecryption = Cipher.getInstance("Serpent/CBC/PKCS5Padding", new BouncyCastleProvider());
        cipherSerpentEncryption = Cipher.getInstance("Serpent/CBC/PKCS5Padding", new BouncyCastleProvider());

        publicKey = keyPair.getPublic();
    }
}
