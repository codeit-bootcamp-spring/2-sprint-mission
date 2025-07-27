package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // 메모리 기반 SimpleBroker 활성화 (구독 경로: /sub)
    config.enableSimpleBroker("/sub");
    // 애플리케이션 메시지 발행 경로: /pub
    config.setApplicationDestinationPrefixes("/pub");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // STOMP 엔드 포인트 /ws 설정, SockJS 지원
    registry.addEndpoint("/ws")
        .setAllowedOrigins("http://localhost:3000") // CORS 설정
        .withSockJS();
  }
}
