package edu.bsu.sn.client;

import edu.bsu.sn.client.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({SecurityProperties.class})
public class SNClientApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SNClientApplication.class).headless(false).run(args);
    }

}
