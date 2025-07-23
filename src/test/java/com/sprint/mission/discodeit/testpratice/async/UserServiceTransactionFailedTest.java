package com.sprint.mission.discodeit.testpratice.async;

import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.s3.S3Adapter;
import com.sprint.mission.discodeit.security.config.AdminInitializer;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.groups.Tuple;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

//@Disabled
public class UserServiceTransactionFailedTest extends IntegrationTestSupport {

  @Autowired
  private UserService userService;
  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @MockitoBean
  private AdminInitializer adminInitializer;
  //  @MockitoBean
  @Autowired
  private UserRepository userRepository;

  @MockitoBean
  private S3Client s3Client;

  @AfterEach
  void tearDown() {
    binaryContentRepository.deleteAllInBatch();
    userRepository.deleteAllInBatch();
  }


  // 통제하기가 힘들다 이런 말만 남겨 놓자. computableFuture 내부에서는 다른 쓰레드여서 그런가 저장이 안되더라
  // 다른 쓰레드가 작업 중이면 안되는건가?
  // 그럼 aftercommit하면 되나?
  @DisplayName("커밋 후 이미지 업로드 설정을 하지 않는다면, 트랜잭션이 실패해도 바이너리 컨텐츠는 저장됩니다.")
  @Test
  void test_TransactionSynchronizationManager() {
    // given
    String fileName = UUID.randomUUID().toString();
    UserCreateRequest userRequest = new UserCreateRequest("hwnaghwang", "hwnaghwang@naver.com",
        "hwnaghwang");
    BinaryContentRequest binaryContentRequest = new BinaryContentRequest(fileName, "jpg", 10,
        fileName.getBytes());

    PutObjectResponse mockResponse = PutObjectResponse.builder()
        .eTag("mock-etag")
        .build();
    BDDMockito.given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .willReturn(mockResponse);
//    BDDMockito.given(userRepository.save(any())).willThrow(IllegalArgumentException.class);
    userService.register(userRequest, binaryContentRequest);

    // when & then
//    SoftAssertions.assertSoftly(soft -> {
////      soft.assertThatThrownBy(() -> userService.register(userRequest, binaryContentRequest))
////          .isInstanceOf(IllegalArgumentException.class);
//      // 제대로 하려면? await를 줘야되네
//      // 그 보려면 await를 받아야해
//      soft.assertThat(binaryContentRepository.findAll()) // 여기서는 왜 안들어가지? 저장이 되야되는거 아닌가?
//          .hasSize(1)
//          .extracting(BinaryContent::getFileName, BinaryContent::getBinaryContentUploadStatus)
//          .containsExactlyInAnyOrder(Tuple.tuple(fileName, BinaryContentUploadStatus.SUCCESS));
//    });

    Awaitility.await()
        .atMost(Duration.ofSeconds(10))
        .pollInterval(Duration.ofMillis(1000))
        .untilAsserted(() -> {
          Assertions.assertThat(binaryContentRepository.findAll()) // 여기서는 왜 안들어가지? 저장이 되야되는거 아닌가?
              .hasSize(1)
              .extracting(BinaryContent::getFileName, BinaryContent::getBinaryContentUploadStatus)
              .containsExactlyInAnyOrder(Tuple.tuple(fileName, BinaryContentUploadStatus.SUCCESS));
        });
  }

}
