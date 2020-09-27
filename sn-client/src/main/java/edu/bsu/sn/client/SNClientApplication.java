package edu.bsu.sn.client;

import edu.bsu.sn.client.console.processor.ConsoleProcessor;
import edu.bsu.sn.client.security.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@RequiredArgsConstructor
@EnableConfigurationProperties({SecurityProperties.class})
public class SNClientApplication {
    private final ConsoleProcessor consoleProcessor;

    public static void main(String[] args) {
        new SpringApplicationBuilder(SNClientApplication.class).headless(false).run(args);
    }

    @EventListener
    public void startConsole(ContextRefreshedEvent contextRefreshedEvent) {
        consoleProcessor.runConsole();
    }
}
