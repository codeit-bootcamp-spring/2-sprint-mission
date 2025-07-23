package com.sprint.mission.discodeit.event.handler;

import com.sprint.mission.discodeit.event.BinaryContentUploadedEvent;
import com.sprint.mission.discodeit.service.async.AsyncBinaryContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentEventHandler {

  private final AsyncBinaryContentService asyncBinaryContentService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleUploadEvent(BinaryContentUploadedEvent event) {
    asyncBinaryContentService.uploadBinaryContent(event.contentId(), event.bytes());
  }

}
