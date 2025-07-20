package com.sprint.mission.discodeit.common.s3;

import static com.sprint.mission.discodeit.common.filter.constant.LogConstant.REQUEST_ID;
import static org.mockito.Mockito.atLeastOnce;

import com.sprint.mission.discodeit.common.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.common.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.time.Duration;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

class S3AdapterTest extends IntegrationTestSupport {

  @Value("${discodeit.storage.s3.presigned-url-expiration}")
  private long presignedUrlExpirationSeconds;
  @Value("${discodeit.storage.s3.bucket}")
  private String bucket;
  private static final String UPLOAD_FILE_TYPE = "image/jpeg";

  @MockitoBean
  private S3Client s3Client;
  @Autowired
  private AsyncTaskFailureRepository asyncTaskFailureRepository;
  @Autowired
  private S3Adapter s3Adapter;

  @AfterEach
  void tearDown() {
    asyncTaskFailureRepository.deleteAllInBatch();
    MDC.clear();
  }

  @DisplayName("MaxRetryAttempt를 넘으면, Retry로직이 실행된다.")
  @Test
  void putTest() throws InterruptedException {
    // given
    String key = UUID.randomUUID().toString();
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .contentType(UPLOAD_FILE_TYPE)
        .build();
    byte[] bytes = "Hello".getBytes();
    String requestId = UUID.randomUUID().toString();
    MDC.put(REQUEST_ID, requestId);
    BDDMockito.given(
            s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class)))
        .willThrow(SdkClientException.class);

    // when
    s3Adapter.put(putRequest, RequestBody.fromBytes(bytes));

    // then
    Awaitility.await()
        .atMost(Duration.ofSeconds(5))
        .pollInterval(Duration.ofMillis(1000))
        .untilAsserted(() -> {
          Assertions.assertThat(asyncTaskFailureRepository.findAll())
              .extracting(AsyncTaskFailure::getRequestId)
              .containsExactly(requestId);
        });

    Mockito.verify(s3Client, atLeastOnce())
        .putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class));
  }

}