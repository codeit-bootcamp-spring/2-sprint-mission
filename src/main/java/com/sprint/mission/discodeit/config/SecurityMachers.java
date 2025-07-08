package com.sprint.mission.discodeit.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SecurityMachers {

    public static final RequestMatcher NON_API = new NegatedRequestMatcher(
        new AntPathRequestMatcher("/api/**"));

    public static final RequestMatcher CSRF_TOKEN = new AntPathRequestMatcher(
        "/api/auth/csrf-token", HttpMethod.GET.name());

    public static final RequestMatcher LOGOUT = new AntPathRequestMatcher("/api/auth/logout",
        HttpMethod.POST.name());

    public static final RequestMatcher SIGN_UP = new AntPathRequestMatcher("/api/users",
        HttpMethod.POST.name());

    public static final RequestMatcher USERS_LIST = new AntPathRequestMatcher("/api/users",
        HttpMethod.GET.name());

    public static final RequestMatcher CHANNELS_LIST = new AntPathRequestMatcher("/api/channels",
        HttpMethod.GET.name());

    public static final RequestMatcher AUTH_ME = new AntPathRequestMatcher("/api/auth/me",
        HttpMethod.GET.name());

    public static final RequestMatcher LOGIN = new AntPathRequestMatcher("/api/auth/login",
        HttpMethod.POST.name());
}


