package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final String bucket;
    private final S3Presigner s3Presigner;
    private final int presignedUrlExpiration; // 초 단위

    @Override
    public UUID put(UUID id, byte[] bytes) {
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(id.toString())
            .contentLength((long) bytes.length)
            .build();
        s3Client.putObject(request, RequestBody.fromBytes(bytes));
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        GetObjectRequest request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(id.toString())
            .build();
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
        return response;
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto dto) {
        String key = dto.id().toString();

        // 1. Presigned URL 발급
        GetObjectRequest getReq = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();
        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(
            GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
                .getObjectRequest(getReq)
                .build()
        );

        // 2. 302 리다이렉트 응답
        URI redirectUri = URI.create(presigned.url().toString());
        return ResponseEntity.status(HttpStatus.FOUND)
            .location(redirectUri)
            .build();
    }
}
