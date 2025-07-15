package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Optional<String> optionalAccessToken = resolveToken(request);
        if(optionalAccessToken.isPresent() && !isAllowedPath(request)) {
            String accessToken = optionalAccessToken.get();
            if(jwtService.validate(accessToken)) {
                UserDto userDto = jwtService.parse(accessToken).userDto();
                DiscodeitUserDetails userDetails = new DiscodeitUserDetails(userDto, null);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            }else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            }
        } else{
            filterChain.doFilter(request, response);
        }

    }

    private Optional<String> resolveToken(HttpServletRequest request) {
        String bearer = "Bearer ";
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith(bearer)) {
            return Optional.of(bearerToken.substring(bearer.length()));
        }
        else{
            return Optional.empty();
        }
    }

    private boolean isAllowedPath(HttpServletRequest request) {
        return Arrays.stream(SecurityMatchers.PUBLIC_MATCHERS)
                .anyMatch(matcher -> matcher.matches(request));
    }


}
