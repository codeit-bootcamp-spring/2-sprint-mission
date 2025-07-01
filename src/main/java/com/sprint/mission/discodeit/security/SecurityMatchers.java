package com.sprint.mission.discodeit.security;

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
      "/api/auth/csrf-token", HttpMethod.GET.name());

  public static final RequestMatcher SIGN_UP = new AntPathRequestMatcher(
      "/api/users", HttpMethod.POST.name());

  public static final RequestMatcher LOGIN = new AntPathRequestMatcher(
      "/api/auth/login", HttpMethod.POST.name()
  );

  public static final RequestMatcher LOGOUT = new AntPathRequestMatcher(
      "/api/auth/logout", HttpMethod.POST.name()
  );

  public static final RequestMatcher CREATE_CHANNEL = new AntPathRequestMatcher(
      "/api/channels/public", HttpMethod.POST.name()
  );

  public static final RequestMatcher UPDATE_CHANNEL = new AntPathRequestMatcher(
      "/api/channels/{channelId}", HttpMethod.PATCH.name()
  );

  public static final RequestMatcher DELETE_CHANNEL = new AntPathRequestMatcher(
      "/api/channels/{channelId}", HttpMethod.DELETE.name()
  );

  public static final RequestMatcher UPDATE_ROLE = new AntPathRequestMatcher(
      "/api/auth/role", HttpMethod.PUT.name()
  );

  public static final String LOGIN_URL = "/api/auth/login";
}
