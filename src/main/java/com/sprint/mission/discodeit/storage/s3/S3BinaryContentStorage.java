package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3StorageProperties properties;

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(properties.bucket())
                        .key(binaryContentId.toString())
                        .build(),
                RequestBody.fromBytes(bytes));
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        return s3Client.getObject(GetObjectRequest.builder()
                .bucket(properties.bucket())
                .key(binaryContentId.toString())
                .build());
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto metaData) {
        URL presignedUrl = s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
                        .signatureDuration(properties.presignedUrlExpiry())
                        .getObjectRequest(GetObjectRequest.builder()
                                .bucket(properties.bucket())
                                .key(metaData.id().toString())
                                .build())
                        .build())
                .url();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(presignedUrl.toString()))
                .build();
    }
}

