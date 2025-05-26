package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@ExtendWith(MockitoExtension.class)
public class S3BinaryContentStorageTest {

  @Mock
  private S3Client s3Client;

  @Mock
  private S3Presigner s3Presigner;

  private S3BinaryContentStorage storage;
  private final String bucket = "test";

  @BeforeEach
  public void setUp() {
    storage = new S3BinaryContentStorage(bucket, s3Client, s3Presigner);
  }

  @Test
  void putFile_success() {
    UUID id = UUID.randomUUID();
    byte[] fileData = bucket.getBytes();

    ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
    storage.put(id, fileData);

    verify(s3Client).putObject(captor.capture(), any(RequestBody.class));

    PutObjectRequest capturedRequest = captor.getValue();

    assertThat(capturedRequest.bucket()).isEqualTo(bucket);
    assertThat(capturedRequest.key()).isEqualTo(id.toString());
  }

  @Test
  void download_shouldReturn302WithPresignedUrlLocation_bddStyle() throws MalformedURLException {
    UUID id = UUID.randomUUID();
    String contentType = "image/png";
    String fileName = "pic.png";
    String expectedUrl = "https://presigned.example.com/" + id;
    BinaryContentDto meta = new BinaryContentDto(id, fileName, 12L, contentType);

    PresignedGetObjectRequest presigned = Mockito.mock(PresignedGetObjectRequest.class);

    given(presigned.url()).willReturn(new URL(expectedUrl));

    given(s3Presigner.presignGetObject(
        argThat((GetObjectPresignRequest req) ->
            req.getObjectRequest().bucket().equals(bucket) &&
                req.getObjectRequest().key().equals(id.toString()) &&
                req.signatureDuration().equals(Duration.ofMinutes(10))
        )
    )).willReturn(presigned);

    ResponseEntity<Void> response = storage.download(meta);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    assertThat(response.getHeaders().getLocation().toString()).isEqualTo(expectedUrl);
  }

}
