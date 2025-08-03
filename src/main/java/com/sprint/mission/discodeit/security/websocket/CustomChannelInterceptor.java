package com.sprint.mission.discodeit.security.websocket;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import io.jsonwebtoken.JwtException;
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
@RequiredArgsConstructor
@Component
public class CustomChannelInterceptor implements ChannelInterceptor {

  private final JwtService jwtService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
        StompHeaderAccessor.class);

    log.info("WebSocket 메시지 처리: Command={}, MessageType={}, Destination={}, SessionId={}",
        accessor.getCommand(),
        accessor.getMessageType(),
        accessor.getDestination(),
        accessor.getSessionId()
    );

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      try {
        log.info("CONNECT 명령어 처리 시작");
        String accessToken = resolveToken(accessor.getFirstNativeHeader("Authorization"));

        if (accessToken == null) {
          log.error("토큰이 없거나 형식이 잘못됨");
          return message;
          //throw new MessagingException("Missing or invalid Authorization header");
        }

        log.info("토큰 파싱 시작");
        UserDto userDto = jwtService.parsePayload(accessToken).userDto();
        log.info("토큰 파싱 성공: 사용자 ID = {}", userDto.id());

        CustomUserDetails userDetails = new CustomUserDetails(userDto, null);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        auth.setAuthenticated(true);

        accessor.setUser(auth);
        log.info("인증 정보 설정 완료");
      } catch (JwtException | IllegalArgumentException e) {
        log.error("WebSocket 인증 처리 중 오류 발생", e);
        return message;
        //throw new MessagingException("Invalid token for WebSocket connection", e);
      }
    } else {
      log.info("비-CONNECT 명령어 처리: {}", accessor.getCommand());
    }

    return message;
  }

  private String resolveToken(String authHeader) {
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }
    return null;
  }
}
