package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트가 구독할 수 있는 prefix
        config.enableSimpleBroker("/sub");
        // 클라이언트가 메시지를 보낼 때 사용하는 prefix
        config.setApplicationDestinationPrefixes("/pub");
    }

    // STOMP 엔드포인트 등록 (SockJS 지원)
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // /ws 엔드포인트 연결, SockJS 지원
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}