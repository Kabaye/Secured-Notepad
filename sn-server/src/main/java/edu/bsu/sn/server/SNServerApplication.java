package edu.bsu.sn.server;

import edu.bsu.sn.server.security.properties.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SecurityProperties.class})
public class SNServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SNServerApplication.class, args);
    }

}
