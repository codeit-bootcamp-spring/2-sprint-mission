package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

  private static final String REQUEST_ID = "requestId";
  private static final String REQUEST_METHOD = "requestMethod";
  private static final String REQUEST_URI = "requestUri";
  private static final String RESPONSE_HEADER_REQUEST_ID = "Discodeit-Request-ID";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    String requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 8); // 8자리로 예시
    MDC.put(REQUEST_ID, requestId);
    MDC.put(REQUEST_METHOD, request.getMethod());
    MDC.put(REQUEST_URI, request.getRequestURI());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {
    // 응답 헤더에 요청 ID 추가
    String requestId = MDC.get(REQUEST_ID);
    if (requestId != null) {
      response.setHeader(RESPONSE_HEADER_REQUEST_ID, requestId);
    }
    // MDC 정리
    MDC.remove(REQUEST_ID);
    MDC.remove(REQUEST_METHOD);
    MDC.remove(REQUEST_URI);
  }
}