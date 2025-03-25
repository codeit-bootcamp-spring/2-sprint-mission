package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.util.JwtAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JwtAuthInterceptor jwtAuthInterceptor;

    public WebConfig(JwtAuthInterceptor jwtAuthInterceptor) {
        this.jwtAuthInterceptor = jwtAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/servers/create", "/api/servers/delete/**", "/api/servers/update/**")
                .addPathPatterns("/api/channels/create", "/api/channels/delete/**", "/api/channels/update/**")
                .addPathPatterns("/api/messages/create", "/api/messages/delete/**", "/api/messages/update/**")
                .excludePathPatterns("/api/users/register", "/api/users/login"); // 인증 제외 경로
    }
}
