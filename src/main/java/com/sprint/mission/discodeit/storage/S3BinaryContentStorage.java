package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
@ConfigurationProperties(prefix = "discodeit.storage.s3")
@Setter
public class S3BinaryContentStorage implements BinaryContentStorage {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private int presignedUrlExpiration = 600;

    public S3BinaryContentStorage() {
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        S3Client client = getS3Client();
        String key = binaryContentId.toString();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentLength((long) bytes.length)
                .build();

        client.putObject(putRequest, RequestBody.fromBytes(bytes));
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        S3Client client = getS3Client();
        String key = binaryContentId.toString();

        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        try {
            return client.getObject(getRequest);
        } catch (NoSuchKeyException e) {
            throw new NoSuchElementException(binaryContentId + "does not exist");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto metaData) {
        String url = generatePresignedUrl(metaData.id().toString(), metaData.contentType());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    private S3Client getS3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build()) {

            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .responseContentType(contentType)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
                    .getObjectRequest(getRequest)
                    .build();

            return presigner.presignGetObject(presignRequest).url().toString();
        }
    }
}
