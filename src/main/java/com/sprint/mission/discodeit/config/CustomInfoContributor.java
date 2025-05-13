package com.sprint.mission.discodeit.config;

import java.util.Map;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CustomInfoContributor implements InfoContributor {

    private final Environment environment;

    public CustomInfoContributor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("app", Map.of(
                "name", "Discodeit",
                "version", "1.7.0",
                "java-version", System.getProperty("java.version"),
                "spring-boot-version", SpringBootVersion.getVersion()
        ));

        builder.withDetail("datasource", Map.of(
                "url", environment.getProperty("spring.datasource.url"),
                "driver-class-name", environment.getProperty("spring.datasource.driver-class-name")
        ));

        builder.withDetail("jpa", Map.of(
                "ddl-auto", environment.getProperty("spring.jpa.hibernate.ddl-auto")
        ));

        builder.withDetail("storage", Map.of(
                "type", environment.getProperty("discodeit.storage.type"),
                "path", environment.getProperty("discodeit.storage.local.root-path")
        ));

        builder.withDetail("multipart", Map.of(
                "max-file-size", environment.getProperty("spring.servlet.multipart.max-file-size"),
                "max-request-size", environment.getProperty("spring.servlet.multipart.max-request-size")
        ));
    }

}
