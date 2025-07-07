package com.sprint.mission.discodeit.security.config;

import com.sprint.mission.discodeit.domain.auth.service.basic.BasicAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

  private final BasicAuthService basicAuthService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    basicAuthService.initAdmin();
  }

}
