package edu.bsu.sn.server.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("security")
public class SecurityProperties {
    private String password;
}
