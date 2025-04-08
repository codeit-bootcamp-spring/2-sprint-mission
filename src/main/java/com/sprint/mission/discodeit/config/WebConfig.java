package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.jwt.JwtAuthInterceptor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final JwtAuthInterceptor jwtAuthInterceptor;

  @Autowired // 또는 생성자 주입 그대로 사용
  public WebConfig(JwtAuthInterceptor jwtAuthInterceptor) {
    this.jwtAuthInterceptor = jwtAuthInterceptor;
  }

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    registry.addInterceptor(jwtAuthInterceptor)
        .addPathPatterns("/users/**", "/channels/**",   // <--- 수정: /** 추가
            "/messages/**",
            "/binary-contents/**",
            "/api/**", "/read-status/**"
        )  // 기존 '/api/**'도 필요하다면 유지
        .excludePathPatterns("/auth/login", "/auth/register"); // 로그인, 회원가입은 제외
    // 다른 필요한 경로 패턴 추가/제외 가능
  }
}