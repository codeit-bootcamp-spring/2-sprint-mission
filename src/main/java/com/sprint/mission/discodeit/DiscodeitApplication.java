package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DiscodeitApplication {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);


  }

}
