package edu.bsu.sn.client.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("security")
public class SecurityProperties {
    private String password;
}
