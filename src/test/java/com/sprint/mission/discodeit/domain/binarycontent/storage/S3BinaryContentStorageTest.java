package com.sprint.mission.discodeit.domain.binarycontent.storage;

import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.s3.S3Adapter;
import com.sprint.mission.discodeit.s3.exception.S3UploadException;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;


public class S3BinaryContentStorageTest extends IntegrationTestSupport {

  @Autowired
  private BinaryContentStorage binaryContentStorage;
  @Autowired
  private BinaryContentRepository binaryContentRepository;
  @Autowired
  private AsyncTaskFailureRepository asyncTaskFailureRepository;
  @MockitoBean
  private S3Adapter S3Adapter;

  @AfterEach
  void tearDown() {
    binaryContentRepository.deleteAllInBatch();
    asyncTaskFailureRepository.deleteAllInBatch();
  }

  @DisplayName("S3업로드시 예외가 발생하면, 업로드 상태를 Failed로 변경합니다.")
  @Test
  void putUpLoadException() {
    // given
    BinaryContent binaryContent = new BinaryContent("", "", 0);
    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
    byte[] data = "test-data".getBytes(StandardCharsets.UTF_8);
    BDDMockito.given(S3Adapter.put(any(), any()))
        .willReturn(CompletableFuture.failedFuture(new S3UploadException()));

    // when
    UUID binaryContentId = binaryContentStorage.put(savedBinaryContent.getId(), data);

    Awaitility.await()
        .atMost(Duration.ofSeconds(10))
        .pollInterval(Duration.ofMillis(1000))
        .untilAsserted(() -> {
          Assertions.assertThat(binaryContentRepository.findById(binaryContentId))
              .get()
              .extracting(BinaryContent::getBinaryContentUploadStatus)
              .isEqualTo(BinaryContentUploadStatus.FAILED);
        });
  }


  @Disabled
  @DisplayName("UUID와 바이트 배열을 전달하면 S3에 저장된다.")
  @Test
  void put() throws Exception {
    // given
    UUID id = UUID.randomUUID();
    byte[] data = "test-data".getBytes(StandardCharsets.UTF_8);

    // when
    UUID resultId = binaryContentStorage.put(id, data);

    // then
    InputStream inputStream = binaryContentStorage.get(resultId);
    byte[] fetched = inputStream.readAllBytes();
    Assertions.assertThat(new String(fetched)).isEqualTo("test-data");
  }

  @Disabled
  @DisplayName("UUID를 이용하여 S3에 저장된 파일을 읽을 수 있다.")
  @Test
  void get() throws Exception {
    // given
    UUID id = UUID.randomUUID();
    byte[] data = "another-test".getBytes(StandardCharsets.UTF_8);
    binaryContentStorage.put(id, data);

    // when
    InputStream result = binaryContentStorage.get(id);

    // then
    byte[] read = result.readAllBytes();
    Assertions.assertThat(new String(read)).isEqualTo("another-test");
  }

  @Disabled
  @DisplayName("UUID를 이용하여 Presigned URL을 통해 파일 다운로드 경로를 얻을 수 있다.")
  @Test
  void download() {
    // given
    UUID id = UUID.randomUUID();
    binaryContentStorage.put(id, "url-test".getBytes(StandardCharsets.UTF_8));

    // when
    ResponseEntity<?> response = binaryContentStorage.download(id);
    RestTemplate restTemplate = new RestTemplate();
    response.getHeaders().getLocation();
    byte[] downloadedBytes = restTemplate.getForObject(response.getHeaders().getLocation(),
        byte[].class);

    // then
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
      softly.assertThat(new String(downloadedBytes)).isEqualTo("url-test");
    });
  }

}
