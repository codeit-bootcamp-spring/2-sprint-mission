package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.websocket.CustomChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocketSecurity
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final CustomChannelInterceptor customChannelInterceptor;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/sub");
    config.setApplicationDestinationPrefixes("/pub");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*")
        .withSockJS();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(customChannelInterceptor); // 인증 처리
    //@EnableWebSocketSecurity
    // - SecurityContextChannelInterceptor, AuthorizationChannelInterceptor 자동으로 등록해줌
    // - AuthorizationManager<Message<?>> 타입의 빈을 찾아 자동으로 AuthorizationChannelInterceptor에 주입해줌
  }

  @Bean
  public AuthorizationManager<Message<?>> messageAuthorizationManager() {
    return MessageMatcherDelegatingAuthorizationManager.builder()
        .simpDestMatchers("/pub/**").authenticated()
        .simpSubscribeDestMatchers("/sub/channels.**").hasRole("USER")
//        .anyMessage().denyAll()
//        .anyMessage().authenticated()
        .anyMessage().permitAll()
        .build();
  }

}
