package com.sprint.mission.discodeit.testpratice.retry;

import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;

import com.sprint.mission.discodeit.common.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@TestPropertySource(properties = {
    "discodeit.storage.type=s3"
})
class S3RetryTest extends IntegrationTestSupport {

  @MockitoBean
  private S3Client s3Client;
  @Autowired
  private BinaryContentStorage binaryContentStorage;
  @Autowired
  private AsyncTaskFailureRepository asyncTaskFailureRepository;

  @BeforeEach
  void setUp() {
    asyncTaskFailureRepository.deleteAllInBatch();
    MDC.clear();
  }

  @DisplayName("S3에 업로드시 재시도 필요한 예외가 발생하면, 비동기 작업실패로 기록합니다.")
  @Test
  void putExceptionAndRecovery() {
    // given
    MDC.put(REQUEST_ID, "1");
    UUID id = UUID.randomUUID();
    String fileContent = "test-data";
    BDDMockito.given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .willThrow(SdkClientException.builder().build());

    // when & then
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThatThrownBy(() -> binaryContentStorage.put(id, fileContent.getBytes()).get())
          .isInstanceOf(ExecutionException.class)
          .hasCauseInstanceOf(SdkClientException.class);
      softly.assertThat(asyncTaskFailureRepository.findAll())
          .hasSize(1)
          .extracting(AsyncTaskFailure::getRequestId)
          .containsExactly(MDC.get(REQUEST_ID));
    });

    Mockito.verify(s3Client, atLeast(2))
        .putObject(any(PutObjectRequest.class), any(RequestBody.class));
  }

}