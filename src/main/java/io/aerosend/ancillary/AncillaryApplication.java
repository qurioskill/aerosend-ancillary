package io.aerosend.ancillary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AncillaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(AncillaryApplication.class, args);
    }
}
