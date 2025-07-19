package com.sprint.mission.discodeit.common.filter;

import static com.sprint.mission.discodeit.common.filter.constant.logConstant.REQUEST_ID;
import static com.sprint.mission.discodeit.common.filter.constant.logConstant.REQUEST_METHOD;
import static com.sprint.mission.discodeit.common.filter.constant.logConstant.REQUEST_URL;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

  private static final List<String> EXCLUDE_PREFIXES = List.of(
      "/css", "/js", "/images", "/static", "/favicon.ico", "/webjars", "/assets"
  );

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String uri = request.getRequestURI();
    return EXCLUDE_PREFIXES.stream().anyMatch(uri::startsWith);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {

    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");

    // 래퍼로 감싸기 (Body 재사용을 위해)
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

    long start = System.currentTimeMillis();

    try {
      // === MDC 설정 ===
      MDC.put(REQUEST_ID, UUID.randomUUID().toString());
      MDC.put(REQUEST_METHOD, requestWrapper.getMethod());
      MDC.put(REQUEST_URL, requestWrapper.getRequestURI());

      // 필터 체인 호출
      chain.doFilter(requestWrapper, responseWrapper);
    } finally {
      long end = System.currentTimeMillis();

      // === 로그 출력 ===
      log.info("\n" +
              "[REQUEST] {} - {} {} - {}\n" +
              "Headers : {}\n" +
              "Request : {}\n" +
              "Response : {}\n",
          requestWrapper.getMethod(),
          requestWrapper.getRequestURI(),
          responseWrapper.getStatus(),
          (end - start) / 1000.0,
          getHeaders(requestWrapper),
          getRequestBody(requestWrapper),
          getResponseBody(responseWrapper)
      );

      // 응답 복사 (필수!)
      responseWrapper.copyBodyToResponse();

      // MDC 클리어 (메모리 누수 방지)
      MDC.clear();
    }
  }

  private Map<String, String> getHeaders(HttpServletRequest request) {
    Map<String, String> headerMap = new HashMap<>();
    Enumeration<String> headerArray = request.getHeaderNames();
    while (headerArray.hasMoreElements()) {
      String headerName = headerArray.nextElement();
      headerMap.put(headerName, request.getHeader(headerName));
    }
    return headerMap;
  }

  private String getRequestBody(ContentCachingRequestWrapper request) {
    byte[] buf = request.getContentAsByteArray();
    if (buf.length > 0) {
      try {
        return new String(buf, 0, buf.length, request.getCharacterEncoding());
      } catch (UnsupportedEncodingException e) {
        return " - ";
      }
    }
    return " - ";
  }

  private String getResponseBody(ContentCachingResponseWrapper response) {
    byte[] buf = response.getContentAsByteArray();
    if (buf.length > 0) {
      try {
        return new String(buf, 0, buf.length, response.getCharacterEncoding());
      } catch (UnsupportedEncodingException e) {
        return " - ";
      }
    }
    return " - ";
  }

}
