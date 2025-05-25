package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@RequiredArgsConstructor
@Slf4j
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucket;
    private final int expirationSeconds;

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        String key = binaryContentId.toString();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentLength((long) bytes.length)
            .build();
        RequestBody requestBody = RequestBody.fromBytes(bytes);
        s3Client.putObject(putObjectRequest, requestBody);

        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        String key = binaryContentId.toString();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        ResponseBytes<GetObjectResponse> bytes = s3Client.getObject(getObjectRequest,
            ResponseTransformer.toBytes());

        return new ByteArrayInputStream(bytes.asByteArray());
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
        String key = binaryContentDto.id().toString();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject( // Presigned GET 요청 생성
            builder -> builder // Presign 요청 빌더에 대한 람다 표현식 시작
                .signatureDuration(Duration.ofMinutes(5))
                .getObjectRequest(
                    getObjectRequestBuilder -> getObjectRequestBuilder
                        .bucket(bucket)
                        .key(key)
                        .build()
                )
                .build()
        );

        String url = presignedRequest.url().toString();
        return ResponseEntity.
            status(302)
            .header("Location", url)
            .build();
    }
}
