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
  public void registerStompEndpoints(StompEndpointRegistry config) {
    config.addEndpoint("/ws").withSockJS();
  }

  @Override
  //메시지를 어디로 어떻게 보낼지 결정하는 부분
  public void configureMessageBroker(MessageBrokerRegistry config) {
    //어떻게 보낼 지 결정
    config.enableSimpleBroker("/sub");
    //어디로 오는 지 감시
    config.setApplicationDestinationPrefixes("/pub");
  }
}
