package com.sprint.mission.discodeit.domain.binarycontent.storage.s3;

import static com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus.*;

import com.sprint.mission.discodeit.s3.S3Adapter;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.notification.event.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
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
        .orElseThrow(() -> new BinaryContentNotFoundException(Map.of()));

    String key = binaryContentId.toString();
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(UPLOAD_FILE_TYPE)
        .build();

    s3Adapter.put(putRequest, RequestBody.fromBytes(bytes))
        .thenAccept(success -> {
          log.debug("S3 요청 이후 처리 쓰레드 {} ", Thread.currentThread().getName());
          binaryContent.updateUploadStatus(SUCCESS);
          try {
            log.debug("S3서비스에서 데이터 베이스에 저장");
            binaryContentRepository.save(binaryContent);
          } catch (Exception e) {
            log.debug("바이너리 컨텐츠 저장 시 에러 : {}", e.getMessage());
          }

        })
        .exceptionally(failure -> {
          binaryContent.updateUploadStatus(FAILED);
          binaryContentRepository.save(binaryContent);
          publishAsyncFailedEvent();
          return null;
        });

    return binaryContentId;
  }

  private void publishAsyncFailedEvent() {
    SecurityContext context = SecurityContextHolder.getContext();
    UserResult userResult = (UserResult) context.getAuthentication().getPrincipal();

    AsyncFailedNotificationEvent asyncFailedNotificationEvent = new AsyncFailedNotificationEvent(
        userResult.id(),
        "S3"
    );
    eventPublisher.publishEvent(asyncFailedNotificationEvent);
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
