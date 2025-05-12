package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.auth.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final JwtAuthInterceptor jwtAuthInterceptor;
  private final MDCLoggingInterceptor mdcLoggingInterceptor;

//  @Override
//  public void addInterceptors(InterceptorRegistry registry) {
//    registry.addInterceptor(jwtAuthInterceptor)
//        .addPathPatterns("/api/servers/**")
//        .addPathPatterns("/api/channelList/**")
//        .addPathPatterns("/api/messages/**")
//        .excludePathPatterns("/api/users/register", "/api/users/login"); // 인증 제외 경로
//  }
}

