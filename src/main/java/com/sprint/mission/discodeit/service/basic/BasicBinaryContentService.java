package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.SseService;
import com.sprint.mission.discodeit.service.event.FileUploadEvent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final ApplicationEventPublisher eventPublisher;
  private final SseService sseService;

  @Transactional
  @Override
  public BinaryContent create(BinaryContentCreateRequest request) {
    log.debug("바이너리 컨텐츠 생성 및 이벤트 발행 시작");

    BinaryContent binaryContent = new BinaryContent(
        request.fileName(),
        (long) request.bytes().length,
        request.contentType()
    );
    binaryContentRepository.save(binaryContent);

    UUID userId = null;
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof DiscodeitUserDetails userDetails) {
      userId = userDetails.getUserDto().id();
    }

    eventPublisher.publishEvent(new FileUploadEvent(binaryContent.getId(), request.bytes(), userId));

    log.info("바이너리 컨텐츠 생성 및 업로드 이벤트 발행 완료: id={}", binaryContent.getId());
    return binaryContent;
  }

  @Async("fileUploadTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Retryable(
      value = { IOException.class, RuntimeException.class },
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000)
  )
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleFileUpload(FileUploadEvent event) {
    UUID binaryContentId = event.binaryContentId();
    log.info("파일 업로드 이벤트 수신 (비동기 처리 시작): id={}", binaryContentId);

    binaryContentStorage.put(binaryContentId, event.bytes());

    BinaryContent content = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> BinaryContentNotFoundException.withId(binaryContentId));
    content.changeStatusToSuccess();
    log.info("비동기 파일 업로드 성공 및 상태 SUCCESS로 업데이트: id={}", binaryContentId);

    if (event.userId() != null) {
      sseService.send(event.userId(), "binaryContents.status", binaryContentMapper.toDto(content));
    }
  }

  @Recover
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void recoverUploadFailure(Throwable e, FileUploadEvent event) {
    UUID binaryContentId = event.binaryContentId();
    String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID);
    AsyncTaskFailure failureInfo = new AsyncTaskFailure("file-upload", requestId, e.getMessage());
    log.error("파일 업로드 최종 실패. 복구 로직 실행. 실패 정보: {}", failureInfo, e);

    binaryContentRepository.findById(binaryContentId).ifPresent(content -> {
      content.changeStatusToFailed();
      log.info("파일 업로드 실패 상태 FAILED로 업데이트: id={}", content.getId());

      if (event.userId() != null) {
        sseService.send(event.userId(), "binaryContents.status", binaryContentMapper.toDto(content));
      }
    });
  }

  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    log.debug("바이너리 컨텐츠 조회 시작: id={}", binaryContentId);
    BinaryContentDto dto = binaryContentRepository.findById(binaryContentId)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> BinaryContentNotFoundException.withId(binaryContentId));
    log.info("바이너리 컨텐츠 조회 완료: id={}, fileName={}",
        dto.id(), dto.fileName());
    return dto;
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    log.debug("바이너리 컨텐츠 목록 조회 시작: ids={}", binaryContentIds);
    List<BinaryContentDto> dtos = binaryContentRepository.findAllById(binaryContentIds).stream()
        .map(binaryContentMapper::toDto)
        .toList();
    log.info("바이너리 컨텐츠 목록 조회 완료: 조회된 항목 수={}", dtos.size());
    return dtos;
  }

  @Transactional
  @Override
  public void delete(UUID binaryContentId) {
    log.debug("바이너리 컨텐츠 삭제 시작: id={}", binaryContentId);
    if (!binaryContentRepository.existsById(binaryContentId)) {
      throw BinaryContentNotFoundException.withId(binaryContentId);
    }
    binaryContentRepository.deleteById(binaryContentId);
    log.info("바이너리 컨텐츠 삭제 완료: id={}", binaryContentId);
  }
}