package edu.bsu.sn.client.web.client;

import edu.bsu.sn.client.web.model.LogInUser;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

@Component
public class SecuredNotepadClient {
    private final RestTemplate restTemplate;

    public SecuredNotepadClient() {
        this.restTemplate = new RestTemplate();
    }

    @SneakyThrows
    public SecretKey askForNewSessionKey() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        byte[] body = restTemplate.exchange("http://localhost:8280/api/v1/security/register", HttpMethod.POST,
                new HttpEntity<>(new LogInUser().setPublicKey(keyPair.getPublic().getEncoded())
                        .setLogin("Vova")), byte[].class)
                .getBody();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        byte[] result = cipher.doFinal(body);

        return new SecretKeySpec(result, "AES");
    }
}
