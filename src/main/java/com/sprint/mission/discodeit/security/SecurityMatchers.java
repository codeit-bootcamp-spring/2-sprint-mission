package com.sprint.mission.discodeit.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public final class SecurityMatchers {

  private SecurityMatchers() {

  }

  public static final RequestMatcher GET_CSRF_TOKEN = new AntPathRequestMatcher(
      "/api/auth/csrf-token", HttpMethod.GET.name());

  public static final RequestMatcher SIGN_UP = new AntPathRequestMatcher(
      "/api/users", HttpMethod.POST.name());

  public static final RequestMatcher PUBLIC = new AntPathRequestMatcher(
      "/api/channels/public", HttpMethod.POST.name());

  public static final RequestMatcher LOGIN = new AntPathRequestMatcher(
      "/api/auth/login", HttpMethod.POST.name()
  );

  public static final RequestMatcher DOWNLOAD = new AntPathRequestMatcher(
      "/api/binaryContents/**", HttpMethod.GET.name()
  );

  public static final RequestMatcher LOGOUT = new AntPathRequestMatcher(
      "/api/auth/logout", HttpMethod.POST.name()
  );

  public static final RequestMatcher ACTUATOR = new AntPathRequestMatcher(
      "/actuator/**"
  );

  public static RequestMatcher REFRESH = new AntPathRequestMatcher(
      "/api/auth/refresh", HttpMethod.POST.name()
  );

  public static final RequestMatcher CACHE = new AntPathRequestMatcher(
      "/api/cache/stats", HttpMethod.GET.name()
  );

  public static final RequestMatcher FRONT = new OrRequestMatcher(
      new AntPathRequestMatcher("/", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/error", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/index.html", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/assets/**", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/favicon.ico", HttpMethod.GET.name())
  );

  public static final String LOGIN_URL = "/api/auth/login";
  public static final String LOGOUT_URL = "/api/auth/logout";
}
