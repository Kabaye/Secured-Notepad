package edu.bsu.sn.server.security.ks;

import edu.bsu.sn.server.security.properties.SecurityProperties;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CustomKeyStore {
    private final ConcurrentHashMap<String, PublicKey> usersKeys;
    private final KeyStore keyStore;
    private final SecurityProperties securityProperties;

    @SneakyThrows
    public CustomKeyStore(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, securityProperties.getPassword().toCharArray());
        usersKeys = new ConcurrentHashMap<>();
    }
}
