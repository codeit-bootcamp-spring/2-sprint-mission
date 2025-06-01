package com.sprint.mission.discodeit.aop;

import com.sprint.mission.discodeit.dto.controller.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.controller.user.CreateUserRequestDTO;
import com.sprint.mission.discodeit.util.MaskingUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  @Around("@annotation(com.sprint.mission.discodeit.aop.LogMasking)")
  public Object logMasking(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();

    for (Object arg : args) {
      if (arg instanceof LoginRequestDTO dto) {
        String maskedUsername = MaskingUtil.maskUsername(dto.username());
        log.info("Login request (username = {})", maskedUsername);
      } else if (arg instanceof CreateUserRequestDTO dto) {
        String maskedUsername = MaskingUtil.maskUsername(dto.username());
        String maskedEmail = MaskingUtil.maskEmail(dto.email());
        log.info("User create request (username: {}, email: {})", maskedUsername, maskedEmail);
      }
    }
    return joinPoint.proceed(args);
  }
}
