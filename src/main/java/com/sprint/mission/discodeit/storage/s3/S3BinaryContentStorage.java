package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.async.AsyncRepository;
import com.sprint.mission.discodeit.async.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.storage.BinaryStorageDownloadException;
import com.sprint.mission.discodeit.exception.storage.BinaryStoragePutException;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Component
@ConditionalOnProperty(
    name = "discodeit.storage.type",
    havingValue = "s3"
)
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final AsyncRepository asyncRepository;

    @Value("${discodeit.storage.s3.bucket}")
    private String BUCKET_NAME;

    @Value("${discodeit.storage.s3.presigned-url-expiration}")
    private long PRESIGED_URL_EXPIRATION;

    @Override
    @Async("fileUploadExecutor")
    @Retryable(
        value = {BinaryStoragePutException.class},
        maxAttempts = 3,
        backoff = @Backoff(
            delay = 2000L,
            multiplier = 2
        )
    )
    public CompletableFuture<UUID> put(UUID id, byte[] bytes) {
        try {
            log.info("업로드 지연 시작: id={}", id);
            Thread.sleep(3000); // 3초 지연
            log.info("업로드 지연 종료: id={}", id);

            log.info("파일 업로드 진행 시작: id={},", id);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(id.toString())
                .build();

            log.info("파일 업로드 진행중: id={}", id);
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

            log.info("파일 업로드 완료: id={}", id);
            return CompletableFuture.completedFuture(id);
        } catch (S3Exception e) {
            throw new RuntimeException("S3에 파일 업로드 실패: " + e.awsErrorDetails().errorMessage(), e);
        } catch (SdkClientException e) {
            throw new RuntimeException("AWS SDK 내부 오류 발생", e);
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 중 알 수 없는 오류 발생", e);
        }
    }

    @Override
    public InputStream get(UUID id) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(id.toString())
                .build();

            return s3Client.getObject(getObjectRequest);
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("아이디에 해당하는 파일을 찾는 중 오류 발생: " + id, e);
        } catch (S3Exception e) {
            throw new RuntimeException("S3에서 파일을 가져오는 중 오류 발생", e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try {
            String encodedFileName = URLEncoder.encode(binaryContentDto.fileName(),
                    StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

            String contentDisposition =
                "attachment; filename=\"download\"; filename*=UTF-8''" + encodedFileName;

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(binaryContentDto.id().toString())
                .responseContentDisposition(contentDisposition)
                .responseContentType(binaryContentDto.contentType())
                .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(builder ->
                builder.signatureDuration(Duration.ofSeconds(PRESIGED_URL_EXPIRATION))
                    .getObjectRequest(getObjectRequest)
            );

            return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, presignedRequest.url().toString())
                .build();

        } catch (Exception e) {
            throw BinaryStorageDownloadException.forId(binaryContentDto.id().toString(), e);
        }
    }

    @Recover
    public CompletableFuture<UUID> recoverPut(BinaryStoragePutException e, UUID id, byte[] bytes) {
        String taskName = Thread.currentThread().getName();
        String requestId = MDC.get("requestId");

        log.error("업로드 실패 복구: id={}, requestId={}", id, requestId);

        AsyncTaskFailure AsyncTaskFailure = new AsyncTaskFailure(
            taskName,
            requestId,
            e.getMessage()
        );

        asyncRepository.save(AsyncTaskFailure);
        return CompletableFuture.completedFuture(id);
    }
}
