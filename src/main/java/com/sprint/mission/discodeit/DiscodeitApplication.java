package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
@EnableCaching
@ConfigurationPropertiesScan
public class DiscodeitApplication {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
  }
}
