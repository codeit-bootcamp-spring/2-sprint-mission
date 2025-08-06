package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
public class WebSocketSecurityConfig {

  @Bean
  public AuthorizationManager<Message<?>> messageAuthorizationManager() {
    return MessageMatcherDelegatingAuthorizationManager.builder()
        .simpSubscribeDestMatchers("/sub/**").authenticated()
        .simpDestMatchers("/pub/**").authenticated()
        .anyMessage().permitAll()
        .build();
  }
}