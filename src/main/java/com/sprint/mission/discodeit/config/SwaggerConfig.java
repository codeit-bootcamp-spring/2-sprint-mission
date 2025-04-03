package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("springdoc-openapi")
            .version("1.0")
            .description("springdoc-openapi swagger-ui 화면입니다.")
        );
  }

  @Bean
  public GroupedOpenApi api() {
    String[] paths = {"/api/**"};
    String[] packagesToScan = {"com.sprint.mission"};
    return GroupedOpenApi.builder()
        .group("springdoc-openapi")
        .pathsToMatch(paths)
        .packagesToScan(packagesToScan)
        .build();
  }
}
