package edu.bsu.sn.server.security.ks;

import edu.bsu.sn.server.security.properties.SecurityProperties;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class CustomKeyStore {

    /**
     * Second parameter is entry of cipher expiration and entry of cipher for encryption and cipher for decryption
     */
    private final ConcurrentHashMap<String, Map.Entry<Instant, Map.Entry<Cipher, Cipher>>> usersCiphers;
    private final SecurityProperties securityProperties;

    @SneakyThrows
    public CustomKeyStore(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        usersCiphers = new ConcurrentHashMap<>();
    }
}
