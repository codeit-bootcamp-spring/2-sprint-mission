package com.sprint.mission.discodeit.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.event.AsyncTaskFailureEvent;
import com.sprint.mission.discodeit.event.BinaryContentCreateEvent;
import com.sprint.mission.discodeit.event.BinaryContentUploadFailureEvent;
import com.sprint.mission.discodeit.event.BinaryContentUploadSuccessEvent;
import com.sprint.mission.discodeit.event.sse.SseBinaryContentStatusEvent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentUploadEventListener {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentMapper binaryContentMapper;
  private final ApplicationEventPublisher eventPublisher;
  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "binary-content.upload-success", groupId = "binary-content-group")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleUploadSuccess(String kafkaEvent) throws JsonProcessingException {
    BinaryContentUploadSuccessEvent event = objectMapper.readValue(kafkaEvent,
        BinaryContentUploadSuccessEvent.class);
    log.info("업로드 성공 이벤트 처리 시작: binaryContentId={}, requestId={}",
        event.binaryContentId(), event.requestId());

    try {
      BinaryContent binaryContent = binaryContentRepository.findById(event.binaryContentId())
          .orElseThrow(() -> BinaryContentNotFoundException.withId(event.binaryContentId()));

      binaryContent.markAsSuccess();
      binaryContentRepository.save(binaryContent);

      BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);
      eventPublisher.publishEvent(new SseBinaryContentStatusEvent(
          event.userId(),
          binaryContentDto,
          Instant.now()
      ));

      log.info("업로드 성공 상태 업데이트 완료: binaryContentId={}, status=SUCCESS", event.binaryContentId());
    } catch (Exception e) {
      log.error("업로드 성공 이벤트 처리 중 오류 발생: binaryContentId={}", event.binaryContentId(), e);
    }
  }

  @KafkaListener(topics = "binary-content.upload-failure", groupId = "binary-content-group")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleUploadFailure(String kafkaEvent) throws JsonProcessingException {
    BinaryContentUploadFailureEvent event = objectMapper.readValue(kafkaEvent,
        BinaryContentUploadFailureEvent.class);
    log.info("업로드 실패 이벤트 처리 시작: binaryContentId={}, requestId={}",
        event.binaryContentId(), event.requestId());

    try {
      BinaryContent binaryContent = binaryContentRepository.findById(event.binaryContentId())
          .orElseThrow(() -> BinaryContentNotFoundException.withId(event.binaryContentId()));

      binaryContent.markAsFailed();
      binaryContentRepository.save(binaryContent);

      BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);
      eventPublisher.publishEvent(new SseBinaryContentStatusEvent(
          event.userId(),
          binaryContentDto,
          Instant.now()
      ));

      eventPublisher.publishEvent(
          new AsyncTaskFailureEvent(
              event.taskName(),
              event.requestId(),
              event.failureReason()
          )
      );

      eventPublisher.publishEvent(
          new AsyncFailedNotificationEvent(
              event.userId(),
              event.taskName(),
              event.requestId(),
              "파일 업로드 실패"
          )
      );

    } catch (Exception e) {
      log.error("업로드 실패 이벤트 처리 중 오류 발생: requestId={}", event.requestId(), e);
    }
  }

}
