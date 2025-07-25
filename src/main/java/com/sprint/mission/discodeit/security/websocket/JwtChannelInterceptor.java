package com.sprint.mission.discodeit.security.websocket;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

  private final JwtService jwtService;
  private final String AUTH_HEADER = "Authorization";
  private final String TOKEN_PREFIX = "Bearer ";

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    // STOMP CONNECT 단계에서만 인증 처리
    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String authHeader = accessor.getFirstNativeHeader(AUTH_HEADER);

      if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
        String token = authHeader.substring(TOKEN_PREFIX.length());

        if (jwtService.validate(token)) {
          UserDto userDto = jwtService.parse(token).userDto();
          DiscodeitUserDetails userDetails = new DiscodeitUserDetails(userDto, "");

          // 인증된 사용자 정보를 메시지 헤더의 user 정보로 설정
          accessor.setUser(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        }
      }
    }
    return message;
  }
}