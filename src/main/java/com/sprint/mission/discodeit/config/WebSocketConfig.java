package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthChannelInterceptor authChannelInterceptor;

    public WebSocketConfig(AuthChannelInterceptor authChannelInterceptor) {
        this.authChannelInterceptor = authChannelInterceptor;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration
            .interceptors(authChannelInterceptor)
            .interceptors(new SecurityContextChannelInterceptor());
    }

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


    @Bean
    public ChannelInterceptor authorizationChannelInterceptor() {
        MessageMatcherDelegatingAuthorizationManager.Builder builder =
            new MessageMatcherDelegatingAuthorizationManager.Builder();

        builder
            .simpDestMatchers("/pub/admin/**")
            .hasRole("ADMIN")
            .simpDestMatchers("/pub/**")
            .authenticated()
            .simpSubscribeDestMatchers("/sub/**")
            .authenticated();

        AuthorizationManager<Message<?>> manager = builder.build();

        return new AuthorizationChannelInterceptor(manager);
    }
}
