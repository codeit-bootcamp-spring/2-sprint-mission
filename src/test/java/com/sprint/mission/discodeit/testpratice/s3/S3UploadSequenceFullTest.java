package com.sprint.mission.discodeit.testpratice.s3;

import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.time.Duration;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class S3UploadSequenceFullTest extends IntegrationTestSupport {

  private static final String USER_NAME = "hwang";
  private static final String USER_EMAIL = "h@naver.com";
  private static final String USER_PASSWORD = "1234";

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Autowired
  private UserService userService;

  //  @MockitoBean
//  private S3Adapter s3Adapter; // 로컬 스택으로 개선 예정 // 어댑터를 모킹하면 오히려 작동을 안하는데
  @MockitoBean
  private S3Client s3Client;

  @BeforeEach
  void beforeEach() {
    userRepository.deleteAllInBatch();
    binaryContentRepository.deleteAllInBatch();
  }

  @Disabled("s3Adapter를 모킹하면 SUCCESS가 아니라 Waiting이 나옵니다. 비동기 처리시 모킹 주의")
  @DisplayName("프로필 사진과 함께 유저 등록을 요청하면, 프로필 사진을 저장합니다.")
  @Test
  void register_WithProfileImage() {
    // given
    UserCreateRequest userRequest = new UserCreateRequest(USER_NAME, USER_EMAIL, USER_PASSWORD);
    String fileName = UUID.randomUUID().toString();
    BinaryContentRequest binaryContentRequest = new BinaryContentRequest(fileName, "", 0,
        "hello".getBytes());
    PutObjectResponse mockResponse = PutObjectResponse.builder()
        .eTag("mock-etag")
        .build();
//    BDDMockito.given(s3Adapter.put(any(PutObjectRequest.class), any(RequestBody.class)))
//        .willReturn(CompletableFuture.completedFuture(mockResponse));
    BDDMockito.given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .willReturn(mockResponse);

    // when
    UserResult user = userService.register(userRequest, binaryContentRequest);

    // then
    Assertions.assertThat(user)
        .extracting(UserResult::id)
        .isEqualTo(user.id());

    Awaitility.await()
        .atMost(Duration.ofSeconds(12))
        .pollInterval(Duration.ofMillis(1000))
        .untilAsserted(() -> {
          Assertions.assertThat(binaryContentRepository.findAll())
              .hasSize(1)
              .extracting(BinaryContent::getFileName, BinaryContent::getBinaryContentUploadStatus)
              .containsExactlyInAnyOrder(Tuple.tuple(fileName, BinaryContentUploadStatus.SUCCESS));
        });
  }

}
