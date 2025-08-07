package com.sprint.mission.discodeit.domain.binarycontent.storage;

import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;
import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.s3.exception.S3UploadArgumentException;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

@TestPropertySource(properties = {
    "discodeit.storage.type=s3"
})
public class S3BinaryContentStorageTest extends IntegrationTestSupport {

  @Autowired
  private BinaryContentStorage binaryContentStorage;
  @Autowired
  private BinaryContentRepository binaryContentRepository;
  @Autowired
  private AsyncTaskFailureRepository asyncTaskFailureRepository;

  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;

  @BeforeEach
  void setUp() {
    MDC.clear();
    binaryContentRepository.deleteAllInBatch();
    asyncTaskFailureRepository.deleteAllInBatch();
  }

  @DisplayName("파일을 S3에 업로드합니다.")
  @Test
  void putAndGet() throws Exception {
    // given
    MDC.put(REQUEST_ID, "1");
    UUID id = UUID.randomUUID();
    byte[] fileBytes = "url-test".getBytes(StandardCharsets.UTF_8);

    // when
    UUID resultId = binaryContentStorage.put(id, fileBytes).get();

    // then
    InputStream inputStream = binaryContentStorage.get(resultId);
    Assertions.assertThat(inputStream.readAllBytes()).isEqualTo(fileBytes);
  }

  @DisplayName("유효하지 않은 입력이 들어오면 예외를 반환합니다.")
  @Test
  void putExceptionAndThrow() {
    // given
    UUID id = null;
    byte[] fileBytes = "url-test".getBytes(StandardCharsets.UTF_8);

    // when & then
    Assertions.assertThatThrownBy(() -> binaryContentStorage.put(id, fileBytes).get())
        .isInstanceOf(ExecutionException.class)
        .hasCauseInstanceOf(S3UploadArgumentException.class);
  }

  @DisplayName("접근 시간에 제한이 있는 링크를 통해, 업로드한 이미지에 접근할 수 있습니다.")
  @Test
  void download() {
    // given
    UUID id = UUID.randomUUID();
    byte[] fileBytes = "url-test".getBytes(StandardCharsets.UTF_8);
    binaryContentStorage.put(id, fileBytes).join();

    // when
    ResponseEntity<?> response = binaryContentStorage.download(id);

    // then
    RestTemplate restTemplate = new RestTemplate();
    byte[] downloadedBytes = restTemplate.getForObject(response.getHeaders().getLocation(),
        byte[].class);
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(response.getStatusCode().is3xxRedirection()).isTrue();
      softly.assertThat(downloadedBytes).isEqualTo(fileBytes);
    });
  }

}
