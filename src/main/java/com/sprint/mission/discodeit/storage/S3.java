package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@RequiredArgsConstructor
@Slf4j
public class S3 implements BinaryContentStorage{

    private final S3Client s3Client;
    private final BinaryContentRepository binaryContentRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public UUID put(UUID uuid, byte[] bytes) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uuid.toString())
                .build();
        s3Client.putObject(request, RequestBody.fromBytes(bytes));
        return uuid;
    }

    @Override
    public InputStream get(UUID uuid) {
        BinaryContent entity = binaryContentRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Content not found: " + uuid));

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(uuid.toString())
                .build();

        ResponseInputStream<GetObjectResponse> s3Stream = s3Client.getObject(request);
        return s3Stream;
    }

    @Override
    public ResponseEntity<Resource> download(UUID uuid) {
        // DB에서 파일 메타데이터 조회
        BinaryContent entity = binaryContentRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Content not found: " + uuid));

        // S3에서 객체 스트림 획득
        ResponseInputStream<GetObjectResponse> s3Stream = s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(uuid.toString())
                        .build()
        );

        // 스트림을 Resource로 변환
        InputStreamResource resource = new InputStreamResource(s3Stream);
        String filename = entity.getFileName();
        String encodedFilename = UriUtils.encode(filename, StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                .contentLength(entity.getSize())
                .contentType(MediaType.parseMediaType(entity.getContentType()))
                .body(resource);
    }
}
