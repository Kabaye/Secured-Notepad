package edu.bsu.sn.server.security.ks;

import edu.bsu.sn.server.security.properties.SecurityProperties;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class CustomKeyStore {
    private final ConcurrentHashMap<String, Cipher> usersCiphers;
    private final SecurityProperties securityProperties;

    @SneakyThrows
    public CustomKeyStore(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        usersCiphers = new ConcurrentHashMap<>();
    }
}
