package edu.bsu.sn.server.security.configuration;

import edu.bsu.sn.server.security.properties.SecurityProperties;
import java.security.KeyFactory;
import javax.crypto.KeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;
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
    public KeyGenerator keyGeneratorSerpent() {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("Serpent", new org.bouncycastle.jce.provider.BouncyCastleProvider());
        return keyGenerator;
    }

    @Bean
    public TextEncryptor textEncryptor(SecurityProperties securityProperties) {
        final BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(securityProperties.getPassword());
        securityProperties.setPassword("");
        return basicTextEncryptor;

    }
}
