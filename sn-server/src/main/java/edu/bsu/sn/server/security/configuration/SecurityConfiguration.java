package edu.bsu.sn.server.security.configuration;

import edu.bsu.sn.server.security.properties.SecurityProperties;
import java.security.KeyFactory;
import javax.crypto.KeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    @SneakyThrows
    public KeyFactory keyFactoryRSA() {
        return KeyFactory.getInstance("RSA");
    }

    @Bean
    @SneakyThrows
    public KeyGenerator keyGeneratorAES() {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator;
    }
}
