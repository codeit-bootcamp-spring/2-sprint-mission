package com.sprint.mission.discodeit.event.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.security.CustomUserDetailsService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageKafkaEventConsumer {

  private final ObjectMapper objectMapper;
  private final SimpMessagingTemplate messagingTemplate;
  private final CustomUserDetailsService userDetailsService;

  @KafkaListener(topics = "websocket.messages", groupId = "websocket-group")
  public void consume(String messageJson) {
    try {
      MessageDto message = objectMapper.readValue(messageJson, MessageDto.class);
      UUID senderId = message.author().id();

      // SecurityContext 설정
      UserDetails userDetails = userDetailsService.loadUserByUsername(senderId.toString());
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      // Kafka 컨슈머 스레드에 임시로 SecurityContext 설정
      SecurityContextHolder.getContext().setAuthentication(authentication);

      try {
        String destination = String.format("/sub/channels.%s.messages", message.channelId());
        messagingTemplate.convertAndSend(destination, message);
        log.debug("Kafka로부터 수신 후 웹소켓 메시지 전송 완료: destination={}, message={}", destination, message);
      } finally {
        // 작업이 끝나면 SecurityContext 초기화
        SecurityContextHolder.clearContext();
      }
    } catch (JsonProcessingException e) {
      log.error("Kafka 메시지 역직렬화 실패", e);
    }
  }

}
