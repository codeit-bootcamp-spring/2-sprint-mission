package com.sprint.mission.discodeit.domain.binarycontent.storage.s3;

import static com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus.FAILED;
import static com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus.SUCCESS;

import com.sprint.mission.discodeit.common.event.event.S3AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.common.event.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.common.util.s3.S3Adapter;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private static final String UPLOAD_FILE_TYPE = "image/jpeg";

  private final S3Adapter s3Adapter;
  private final BinaryContentRepository binaryContentRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Value("${discodeit.storage.s3.presigned-url-expiration}")
  private long presignedUrlExpirationSeconds;
  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new BinaryContentNotFoundException(Map.of())); // TODO: 7/23/25 S3에서 알 필요가 없다

    String key = binaryContentId.toString();
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(UPLOAD_FILE_TYPE)
        .build();

    s3Adapter.put(putRequest, RequestBody.fromBytes(bytes))
        .thenRun(() -> {
          log.debug("S3 요청 이후 처리 쓰레드 {} ", Thread.currentThread().getName());
          binaryContent.updateUploadStatus(SUCCESS);
          try {
            binaryContentRepository.save(binaryContent);
            log.debug("바이너리 컨텐츠 저장 완료");
          } catch (Exception e) {
            log.debug("바이너리 컨텐츠 저장 시 에러 : {}", e.getMessage());
          }
        })
        .exceptionally(failure -> {
          binaryContent.updateUploadStatus(FAILED);
          binaryContentRepository.save(binaryContent);
          publishAsyncFailedEvent(binaryContent);
          return null;
        });

    return binaryContentId;
  }

  private void publishAsyncFailedEvent(BinaryContent binaryContent) {
    SecurityContext context = SecurityContextHolder.getContext();
    UserResult userResult = (UserResult) context.getAuthentication()
        .getPrincipal(); // TODO: 7/23/25 캐스팅에러 처리필요

    S3AsyncFailedNotificationEvent s3AsyncFailedNotificationEvent = new S3AsyncFailedNotificationEvent(
        userResult.id(), binaryContent);
    eventPublisher.publishEvent(s3AsyncFailedNotificationEvent);
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    String key = binaryContentId.toString();
    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();

    return s3Adapter.get(getRequest);
  }

  @Override
  public ResponseEntity<?> download(UUID binaryContentId) {
    String key = binaryContentId.toString();
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();
    URI downloadUrl = s3Adapter.createDownloadUrl(getObjectRequest, presignedUrlExpirationSeconds);

    return ResponseEntity.status(HttpStatus.FOUND)
        .location(downloadUrl)
        .build();
  }

}
