package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.event.BinaryContentUploadFailureEvent;
import com.sprint.mission.discodeit.event.BinaryContentUploadSuccessEvent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  @Override
  public BinaryContent createEntity(BinaryContentCreateRequest request) {
    String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID);
    log.debug("바이너리 컨텐츠 생성 시작: fileName={}, size={}, contentType={}",
        request.fileName(), request.bytes().length, request.contentType());

    String fileName = request.fileName();
    byte[] bytes = request.bytes();
    String contentType = request.contentType();
    BinaryContent binaryContent = new BinaryContent(
        fileName,
        (long) bytes.length,
        contentType
    );
    binaryContentRepository.save(binaryContent);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
    UUID currentUserId = userDetails.getUserDto().id();

    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
      @Override
      public void afterCommit() {
        CompletableFuture<UUID> uploadFuture = binaryContentStorage.put(
            binaryContent.getId(),
            bytes
        );

        uploadFuture.whenComplete((result, ex) -> {
          if (ex == null) {
            log.info("업로드 성공: binaryContentId={}", result);
            eventPublisher.publishEvent(
                new BinaryContentUploadSuccessEvent(result, requestId, currentUserId)
            );
          } else {
            log.error("업로드 실패: binaryContentId={}", binaryContent.getId(), ex);
            String failureReason = String.format(
                "파일 업로드 실패 - binaryContentId: %s, fileName: %s, error: %s",
                binaryContent.getId(),
                fileName,
                ex.getMessage()
            );
            eventPublisher.publishEvent(
                new BinaryContentUploadFailureEvent(
                    binaryContent.getId(),
                    requestId,
                    "BinaryContentStorage.put",
                    failureReason,
                    currentUserId
                )
            );
          }
        });
      }
    });

    log.info("바이너리 컨텐츠 생성 및 업로드 이벤트 발행: id={}, fileName={}",
        binaryContent.getId(), fileName);
    return binaryContent;
  }

  @Transactional
  @Override
  public BinaryContentDto create(BinaryContentCreateRequest request) {
    BinaryContent binaryContent = createEntity(request);
    return binaryContentMapper.toDto(binaryContent);
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
