package com.sprint.mission.discodeit.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class MDCLoggingInterceptor extends OncePerRequestFilter {

  private static final String HEADER = "Discodeit-Request-ID";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String requestId = request.getHeader("X-Request-ID");
      if (requestId == null) {
        requestId = UUID.randomUUID().toString();
      }
      MDC.put("requestId", requestId);

      String requestURI = request.getRequestURI();
      String method = request.getMethod();
      MDC.put("requestURI", requestURI);
      MDC.put("method", method);

      response.setHeader(HEADER, requestId);
      filterChain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }
}
