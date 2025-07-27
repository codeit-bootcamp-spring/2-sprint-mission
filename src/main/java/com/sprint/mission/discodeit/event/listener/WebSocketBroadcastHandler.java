package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketBroadcastHandler {

  private final MessageService messageService;

  // 비동기보다 인메모리 작업이 더 빠르다.
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleAndBroadcast(NewMessageEvent event) {
    MessageDto createdMessage = event.messageDto();
    UUID channelId = createdMessage.channelId();

    if (channelId == null) {
      log.error("WebSocket 브로드캐스트 실패! 메시지에 채널 ID가 없습니다. messageId={}", createdMessage.id());
      return;
    }

    messageService.broadcastMessage(createdMessage, channelId);
  }

}
