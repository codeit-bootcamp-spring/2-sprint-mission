package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.s3.S3DeleteException;
import com.sprint.mission.discodeit.exception.s3.S3DownloadException;
import com.sprint.mission.discodeit.exception.s3.S3UploadException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.net.MalformedURLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {

  @Mock
  private S3Client s3Client;
  @Mock
  private S3Presigner s3Presigner;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @InjectMocks
  private S3BinaryContentStorage s3BinaryContentStorage;

  @BeforeEach
  void setUp() {
    // @Value 필드 수동 주입
    ReflectionTestUtils.setField(s3BinaryContentStorage, "bucket", "test-bucket");
  }

  @Test
  @DisplayName("s3에 파일 업로드 성공")
  void s3_put_success() {
    // given
    UUID id = UUID.randomUUID();
    byte[] bytes = "test".getBytes();
    PutObjectResponse response = PutObjectResponse.builder().build();
    given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .willReturn(response);
    // when
    s3BinaryContentStorage.put(id, bytes);

    then(s3Client).should(times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
  }

  @Test
  @DisplayName("s3에 파일 업로드 실패")
  void s3_put_failed() {
    // given
    UUID id = UUID.randomUUID();
    byte[] bytes = "test".getBytes();
    given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .willThrow(S3Exception.builder().message("failed").build());

    // when + then
    assertThatThrownBy(() -> s3BinaryContentStorage.put(id, bytes))
        .isInstanceOf(S3UploadException.class);
  }


  @Test
  @DisplayName("s3에서 파일 읽어오기 성공")
  void s3_get_success() {
    // given
    UUID id = UUID.randomUUID();
    byte[] bytes = "test".getBytes();
    ResponseInputStream<GetObjectResponse> mockStream =
        new ResponseInputStream<>(GetObjectResponse.builder().build(),
            new ByteArrayInputStream(bytes));
    given(s3Client.getObject(any(GetObjectRequest.class)))
        .willReturn(mockStream);

    // when
    InputStream result = s3BinaryContentStorage.get(id);

    // then
    assertThat(result).isNotNull();
    then(s3Client).should(times(1)).getObject(any(GetObjectRequest.class));
  }

  @Test
  @DisplayName("s3에서 파일 읽어오기 실패")
  void s3_get_failed() {
    // given
    UUID id = UUID.randomUUID();
    given(s3Client.getObject(any(GetObjectRequest.class)))
        .willThrow(S3Exception.builder().message("failed").build());

    // when + then
    assertThatThrownBy(() -> s3BinaryContentStorage.get(id))
        .isInstanceOf(S3DownloadException.class);
  }

  @Test
  @DisplayName("s3 파일 지우기 성공")
  void s3_delete_success() {
    // given
    UUID id = UUID.randomUUID();
    DeleteObjectResponse response = DeleteObjectResponse.builder().build();
    given(s3Client.deleteObject(any(DeleteObjectRequest.class))).willReturn(response);

    // when
    s3BinaryContentStorage.delete(id);

    // then
    then(s3Client).should(times(1)).deleteObject(any(DeleteObjectRequest.class));
  }

  @Test
  @DisplayName("s3 파일 지우기 실패")
  void s3_delete_failed() {
    // given
    UUID id = UUID.randomUUID();
    given(s3Client.deleteObject(any(DeleteObjectRequest.class)))
        .willThrow(S3Exception.builder().message("failed").build());

    // when + then
    assertThatThrownBy(() -> s3BinaryContentStorage.delete(id))
        .isInstanceOf(S3DeleteException.class);
  }

  @Test
  @DisplayName("s3 파일 다운로드 성공")
  void s3_download_success() throws MalformedURLException {
    // given
    UUID id = UUID.randomUUID();
    String presignedUrl = "https://example.com/file";
    FindBinaryContentResult findResult = new FindBinaryContentResult(id, "test.txt", 5L,
        "text/plain", BinaryContentUploadStatus.SUCCESS);

    PresignedGetObjectRequest presignedRequest = mock(PresignedGetObjectRequest.class);
    given(presignedRequest.url()).willReturn(new URL(presignedUrl));

    given(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
        .willReturn(presignedRequest);

    // when
    ResponseEntity<?> response = s3BinaryContentStorage.download(findResult);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).isEqualTo(presignedUrl);
  }
}