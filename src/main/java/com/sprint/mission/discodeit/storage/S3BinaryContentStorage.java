package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exceptions.ErrorCode;
import com.sprint.mission.discodeit.exceptions.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Repository
@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final BinaryContentJPARepository binaryContentJPARepository;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${discodeit.storage.s3.bucket}")
    private String bucket;

    @Value("${discodeit.storage.s3.presigned-url-expiration}")
    private int presignedUrlExpiration;


    @Override
    public UUID put(UUID id, byte[] bytes) {
        BinaryContent matchingBinaryContent = binaryContentJPARepository.findById(id)
                .orElseThrow(() -> new BinaryContentNotFoundException(Instant.now(), ErrorCode.PROFILE_NOT_FOUND, Map.of("binaryContentId", id)));

        log.info("[S3BinaryContentStorage][put] Uploading file: {}", id);
        String S3Key = matchingBinaryContent.getId() + "-" + matchingBinaryContent.getFileName();
        String contentType = matchingBinaryContent.getContentType();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(S3Key)
                .contentType(contentType)
                .build();

        try {
            s3Client.putObject(putRequest, RequestBody.fromBytes(bytes));
        } catch (S3Exception e) {
            log.error("[S3BinaryContentStorage][put] S3 upload fail: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Uploading fail", e);
        }

        log.info("[S3BinaryContentStorage][put] Complete upload: {}", id);
        return id;
    }


    @Override
    public InputStream get(UUID id) {
        return null;
    }


    @Override
    public ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponse) {
        String S3Key = binaryContentResponse.id() + "-" + binaryContentResponse.fileName();
        String contentType = binaryContentResponse.contentType();

        log.debug("[S3BinaryContentStorage][download] S3 BinaryContent download: binaryContentId={}", binaryContentResponse.id());
        String presignedUrlResponse = generatePresignedUrl(S3Key, contentType);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(presignedUrlResponse))
                .build();
    }


    private String generatePresignedUrl(String key, String contentType) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentType(contentType)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        log.debug("Presigned URL: [{}]", presignedRequest.url().toString());
        log.debug("HTTP method: [{}]", presignedRequest.httpRequest().method());

        return presignedRequest.url().toExternalForm();

    }
}
