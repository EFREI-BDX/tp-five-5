package fr.efreifive.manageplayer;

import fr.efreifive.manageplayer.config.AdminProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AdminProperties.class)
public class ManagePlayerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagePlayerApplication.class, args);
    }
}
