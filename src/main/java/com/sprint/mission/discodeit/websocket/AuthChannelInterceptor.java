package com.sprint.mission.discodeit.websocket;

import com.sprint.mission.discodeit.security.jwt.JwtService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final RoleHierarchy roleHierarchy; // 권한 계층을 주입받습니다.

  public AuthChannelInterceptor(JwtService jwtService, UserDetailsService userDetailsService,
      RoleHierarchy roleHierarchy) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.roleHierarchy = roleHierarchy;
  }


  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
        StompHeaderAccessor.class);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String authHeader = accessor.getFirstNativeHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);

        if (jwtService.validate(token)) {
          String username = jwtService.parse(token).userDto().username();
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          Authentication tempAuth = new UsernamePasswordAuthenticationToken(userDetails, null,
              userDetails.getAuthorities());
          Authentication authentication = new UsernamePasswordAuthenticationToken(
              userDetails, null,
              roleHierarchy.getReachableGrantedAuthorities(tempAuth.getAuthorities())
          );

          accessor.setUser(authentication); // WebSocket 세션에 사용자 설정
          SecurityContextHolder.getContext()
              .setAuthentication(authentication); // SecurityContext에도 설정
        } else {
          throw new AccessDeniedException("Invalid JWT Token");
        }
      } else {
        throw new AccessDeniedException("Authorization header missing");
      }
    }

    return message;
  }
}
