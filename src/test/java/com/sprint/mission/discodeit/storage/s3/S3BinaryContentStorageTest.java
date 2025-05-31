package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    private S3BinaryContentStorage storage;

    private final String bucket = "test-bucket";
    private final int expiration = 600;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storage = new S3BinaryContentStorage(s3Client, bucket, s3Presigner, expiration);
    }

    @DisplayName("put() 메서드는 S3에 파일을 업로드")
    @Test
    void put_shouldUploadFileToS3() {
        // given
        UUID id = UUID.randomUUID();
        byte[] data = "hello".getBytes();

        // when
        UUID result = storage.put(id, data);

        // then
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        assertThat(result).isEqualTo(id);
    }

    @DisplayName("get() 메서드는 S3에서 InputStream을 반환")
    @Test
    void get_shouldReturnInputStreamFromS3() {
        // given
        UUID id = UUID.randomUUID();
        ResponseInputStream<GetObjectResponse> mockStream = new ResponseInputStream<>(
            GetObjectResponse.builder().build(),
            new ByteArrayInputStream("data".getBytes())
        );
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(mockStream);

        // when
        InputStream result = storage.get(id);

        // then
        assertThat(result).isNotNull();
        verify(s3Client).getObject(any(GetObjectRequest.class));
    }

    @DisplayName("download() 메서드는 Presigned URL로 리다이렉트 응답을 반환")
    @Test
    void download_shouldRedirectToPresignedUrl() {
        // given
        UUID id = UUID.randomUUID();
        BinaryContentDto dto = new BinaryContentDto(
            id,
            "example.txt",
            1234L,
            "text/plain"
        );

        URL url = mock(URL.class);
        when(url.toString()).thenReturn("https://s3-url.com/file");

        PresignedGetObjectRequest presigned = mock(PresignedGetObjectRequest.class);
        when(presigned.url()).thenReturn(url);

        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
            .thenReturn(presigned);

        // when
        ResponseEntity<?> response = storage.download(dto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation()).isEqualTo(
            URI.create("https://s3-url.com/file"));
    }

}
