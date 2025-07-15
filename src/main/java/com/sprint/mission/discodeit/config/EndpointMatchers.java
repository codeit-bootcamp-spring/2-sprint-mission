package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EndpointMatchers {

    // 인증
    LOGIN("/api/auth/login", HttpMethod.POST),
    REFRESH("/api/auth/refresh", HttpMethod.POST),
    LOGOUT("/api/auth/logout", HttpMethod.POST),
    AUTH_ME("/api/auth/me", HttpMethod.GET),
    CSRF_TOKEN("/api/auth/csrf-token", HttpMethod.GET),

    // 사용자
    SIGN_UP("/api/users", HttpMethod.POST),

    // 공개 채널 조회
    PUBLIC_CHANNELS_GET("/api/channels/public", HttpMethod.GET),
    PUBLIC_CHANNELS_SEARCH("/api/channels/public/search", HttpMethod.GET),
    CHANNEL_MESSAGES_GET("/api/channels/{channelId}/messages", HttpMethod.GET),

    // 공개 채널 관리
    PUBLIC_CHANNELS_POST("/api/channels/public", HttpMethod.POST),
    PUBLIC_CHANNELS_PATCH("/api/channels/public/{channelId}", HttpMethod.PATCH),
    PUBLIC_CHANNELS_DELETE("/api/channels/public/{channelId}", HttpMethod.DELETE),

    // 비공개 채널 관리
    PRIVATE_CHANNELS_POST("/api/channels/private", HttpMethod.POST),
    PRIVATE_CHANNELS_PATCH("/api/channels/private/{channelId}", HttpMethod.PATCH),
    PRIVATE_CHANNELS_DELETE("/api/channels/private/{channelId}", HttpMethod.DELETE),

    // 채널 멤버 관리
    CHANNEL_MEMBERS("/api/channels/{channelId}/members/**", null),

    // 사용자 권한 수정
    USER_ROLE_UPDATE("/api/users/{userId}/role", HttpMethod.PATCH);


    private final String path;
    private final HttpMethod method;

    public RequestMatcher getMatcher() {
        return method != null
                ? new AntPathRequestMatcher(path, method.name())
                : new AntPathRequestMatcher(path);
    }

    public static RequestMatcher[] getPublicEndpoints() {
        return Arrays.stream(values())
                .filter(endpoint -> Arrays.asList(
                        LOGIN, REFRESH, AUTH_ME, SIGN_UP,
                        PUBLIC_CHANNELS_GET, PUBLIC_CHANNELS_SEARCH, CHANNEL_MESSAGES_GET
                        // SWAGGER_DOCS, SWAGGER_UI, SWAGGER_UI_HTML
                ).contains(endpoint))
                .map(EndpointMatchers::getMatcher)
                .toArray(RequestMatcher[]::new);
    }

    public static RequestMatcher[] getAuthenticatedEndpoints() {
        return Arrays.stream(values())
                .filter(endpoint -> Arrays.asList(
                        PUBLIC_CHANNELS_POST, PUBLIC_CHANNELS_PATCH, PUBLIC_CHANNELS_DELETE,
                        PRIVATE_CHANNELS_POST, PRIVATE_CHANNELS_PATCH, PRIVATE_CHANNELS_DELETE
                ).contains(endpoint))
                .map(EndpointMatchers::getMatcher)
                .toArray(RequestMatcher[]::new);
    }

    public static RequestMatcher[] getChannelManagerEndpoints() {
        return new RequestMatcher[]{CHANNEL_MEMBERS.getMatcher()};
    }

}