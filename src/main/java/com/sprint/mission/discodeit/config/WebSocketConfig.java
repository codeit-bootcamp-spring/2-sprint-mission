package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // STOMP 엔드포인트 설정 (SockJS 지원 포함)
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")          // STOMP 연결 엔드포인트
            .setAllowedOriginPatterns("*") // 모든 origin 허용 (필요 시 제한 가능)
            .withSockJS();                // SockJS fallback 활성화
    }

    // 메시지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // 메모리 기반 SimpleBroker
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트가 서버로 보낼 prefix
    }
}
