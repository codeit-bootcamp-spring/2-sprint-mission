package com.sprint.mission.discodeit.storage.s3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.storage.S3BinaryContentStorage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@SpringBootTest(properties = {
        "discodeit.storage.type=s3"
})
@ActiveProfiles("test")
public class AWSS3Test {

    @Value("${discodeit.storage.s3.access-key}")
    private String accessKey;

    @Value("${discodeit.storage.s3.secret-key}")
    private String secretKey;

    @Value("${discodeit.storage.s3.region}")
    private String region;

    @Value("${discodeit.storage.s3.bucket}")
    private String bucket;

    @Value("${discodeit.storage.s3.presigned-url-expiration}")
    private int expiration;

    private S3BinaryContentStorage storage;

    @BeforeEach
    void setup() throws Exception {
        storage = new S3BinaryContentStorage(accessKey, secretKey, region, bucket, expiration);
    }

    @Test
    void testPutAndGet() throws IOException {
        UUID id = UUID.randomUUID();
        byte[] content = "Hello S3 Test".getBytes();

        UUID storedId = storage.put(id, content);
        assertEquals(id, storedId);

        try (InputStream inputStream = storage.get(id)) {
            assertNotNull(inputStream);
            byte[] readBytes = inputStream.readAllBytes();
            assertArrayEquals(content, readBytes);
        }
    }

    @Test
    void testDownload() throws IOException {
        UUID id = UUID.randomUUID();
        byte[] content = "Download test".getBytes();
        storage.put(id, content);

        BinaryContentDto dto = new BinaryContentDto(id, "sample.txt", (long) content.length, "text/plain");
        ResponseEntity<?> response = storage.download(dto);

        assertEquals(302, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().contains("amazonaws.com"));
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        byte[] content = "Delete test".getBytes();
        storage.put(id, content);

        UUID deletedId = storage.delete(id);
        assertEquals(id, deletedId);

        assertThrows(Exception.class, () -> {
            try (InputStream ignored = storage.get(id)) {
            }
        });
    }
}
