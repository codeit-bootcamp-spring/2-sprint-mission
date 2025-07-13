package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.EndpointMatchers;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.stream(EndpointMatchers.getPublicEndpoints())
                .anyMatch(matcher -> matcher.matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractTokenFromHeader(request);

        if (token == null) {
            sendUnauthorizedResponse(response, "인증 토큰이 필요합니다.");
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            jwtService.validateToken(token).ifPresentOrElse(
                    this::authenticateWithToken,
                    () -> {
                        try {
                            sendUnauthorizedResponse(response, "유효하지 않은 토큰입니다.");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void authenticateWithToken(Claims claims) {
        try {
            UserDto userDto = extractUserDtoFromClaims(claims);
            UserDetails userDetails = userMapper.toEntity(userDto);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new RuntimeException("사용자 정보 추출 실패", e);
        }
    }

    private UserDto extractUserDtoFromClaims(Claims claims) throws IOException {
        String userDtoJson = claims.get("userDto", String.class);
        return objectMapper.readValue(userDtoJson, UserDto.class);
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String errorResponse = objectMapper.writeValueAsString(
                new ErrorResponse(message, "UNAUTHORIZED", 401)
        );
        response.getWriter().write(errorResponse);
    }

    private record ErrorResponse(String message, String code, int status) {
    }
}