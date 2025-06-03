package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.config.S3StorageProperties;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3StorageProperties props;
    private final S3Client s3;
    private final S3Presigner presigner;

    public S3BinaryContentStorage(S3StorageProperties props) {
        this.props = props;

        var credentials = AwsBasicCredentials.create(props.getAccessKey(), props.getSecretKey());
        var provider = StaticCredentialsProvider.create(credentials);
        this.s3 = S3Client.builder()
            .region(Region.of(props.getRegion()))
            .credentialsProvider(provider)
            .build();
        this.presigner = S3Presigner.builder()
            .region(Region.of(props.getRegion()))
            .credentialsProvider(provider)
            .build();
    }

    /* ---------- 업로드 ---------- */
    @Override
    public UUID put(UUID id, byte[] bytes) {
        String key = buildKey(id);
        PutObjectRequest req = PutObjectRequest.builder()
            .bucket(props.getBucket())
            .key(key)
            .build();
        s3.putObject(req, RequestBody.fromBytes(bytes));
        return id; // 그대로 반환
    }

    /* ---------- 다운로드 스트림 ---------- */
    @Override
    public InputStream get(UUID id) {
        GetObjectRequest req = GetObjectRequest.builder()
            .bucket(props.getBucket())
            .key(buildKey(id))
            .build();
        ResponseInputStream<GetObjectResponse> stream = s3.getObject(req);
        return stream;   // 호출측이 close()
    }

    /* ---------- Presigned URL 리다이렉트 ---------- */
    @Override
    public ResponseEntity<Void> download(BinaryContentDto dto) {
        URL url = generatePresignedUrl(buildKey(UUID.fromString(dto.id().toString())),
            dto.contentType());
        return ResponseEntity.status(302)
            .header(HttpHeaders.LOCATION, url.toString())
            .build();
    }

    /* ---------- 유틸 ---------- */
    private String buildKey(UUID id) {
        return "binary/" + id;
    }

    private URL generatePresignedUrl(String key, String contentType) {
        GetObjectRequest get = GetObjectRequest.builder()
            .bucket(props.getBucket())
            .key(key)
            .responseContentType(contentType)
            .build();

        GetObjectPresignRequest presign = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofSeconds(props.getPresignedUrlExpiration()))
            .getObjectRequest(get)
            .build();

        return presigner.presignGetObject(presign).url();
    }
}
