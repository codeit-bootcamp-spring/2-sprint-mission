package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.core.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminInitializer implements ApplicationRunner {

  private final AuthService authService;

  @Override
  public void run(ApplicationArguments args) {
    authService.initAdmin();
    log.info("관리자 계정 초기화");
  }
}
