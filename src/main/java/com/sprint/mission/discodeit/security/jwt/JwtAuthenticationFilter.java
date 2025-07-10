package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.dto.api.ErrorResponse;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            handleUnauthorized(response, null, "Authorization 헤더가 없거나 형식이 잘못되었습니다.");
            return;
        }

        String token = header.replace("Bearer ", "");

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JwtObject jwtObject = JwtObject.toJwtObject(signedJWT);

            if(!jwtService.validateToken(token)) {
                jwtService.invalidateJwtSession(token);
                handleUnauthorized(response, token, "Invalid JWT");
            }

            UserDto userDto = jwtObject.userDto();
            DiscodeitUserDetails userDetails = new DiscodeitUserDetails(userDto, null);
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            handleUnauthorized(response, token, "JWT 처리 실패: " + e.getMessage());
        }
    }

    private void handleUnauthorized(HttpServletResponse response, String token, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            "UNAUTHORIZED",
            message,
            Map.of("accessToken", token),
            HttpStatus.UNAUTHORIZED.toString(),
            HttpStatus.UNAUTHORIZED.value()
        );

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
