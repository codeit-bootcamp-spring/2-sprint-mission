package com.sprint.mission.discodeit.security.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public final class SecurityMatchers {

  private SecurityMatchers() {
  }

  public static final RequestMatcher NON_API = new NegatedRequestMatcher(
      new AntPathRequestMatcher("/api/**"));
  public static final RequestMatcher GET_CSRF_TOKEN = new AntPathRequestMatcher(
      "/api/auth/csrf-token");
  public static final RequestMatcher SIGN_UP = new AntPathRequestMatcher("/api/users",
      HttpMethod.POST.name());
  public static final RequestMatcher LOGOUT = new AntPathRequestMatcher("/api/auth/logout",
      HttpMethod.POST.name());
  public static final RequestMatcher PUBLIC_CHANNEL_ACCESS = new AntPathRequestMatcher(
      "/api/channels/public");
  public static final RequestMatcher ROLE_UPDATE = new AntPathRequestMatcher(
      "/api/auth/role");
  public static final String LOGIN_URL = "/api/auth/login";
  public static final RequestMatcher LOGIN = new AntPathRequestMatcher(LOGIN_URL,
      HttpMethod.POST.name());
  public static final RequestMatcher USER_DELETE = new AntPathRequestMatcher("/api/users/{userId}",
      HttpMethod.DELETE.name());
  public static final RequestMatcher USER_UPDATE = new AntPathRequestMatcher("/api/users/{userId}",
      HttpMethod.PATCH.name());
  public static final RequestMatcher MESSAGE_UPDATE = new AntPathRequestMatcher(
      "/api/messages/{messageId}", HttpMethod.PATCH.name());
  public static final RequestMatcher MESSAGE_DELETE = new AntPathRequestMatcher(
      "/api/messages/{messageId}", HttpMethod.DELETE.name());
  public static final RequestMatcher READ_STATUS_CREATE = new AntPathRequestMatcher(
      "/api/readStatuses", HttpMethod.POST.name());
  public static final RequestMatcher READ_STATUS_UPDATE = new AntPathRequestMatcher(
      "/api/readStatuses/{readStatusId}", HttpMethod.PATCH.name());

  public static final RequestMatcher ME = new AntPathRequestMatcher("/api/auth/me",
      HttpMethod.GET.name());
  public static final RequestMatcher REFRESH = new AntPathRequestMatcher("/api/auth/refresh",
      HttpMethod.POST.name());

  public static final RequestMatcher DOWNLOAD = new AntPathRequestMatcher(
      "/api/binaryContents/{binaryContentId}/download",
      HttpMethod.GET.name());

  public static final RequestMatcher[] PUBLIC_MATCHERS = new RequestMatcher[]{
      NON_API, GET_CSRF_TOKEN, SIGN_UP, LOGOUT, LOGIN, ME, REFRESH, DOWNLOAD
  };

}
