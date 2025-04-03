package com.sprint.mission.discodeit;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@OpenAPIDefinition(info = @Info(
    title = "Discodeit API 문서",
    description = "Discodeit 프로젝트의 Swagger API 문서입니다.",
    version = "1.0.0"), servers = {
    @Server(url = "http://localhost:8080", description = "로컬 서버")})
@SpringBootApplication
public class DiscodeitApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class,
        args);

  }
}
