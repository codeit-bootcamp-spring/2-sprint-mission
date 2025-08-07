package com.sprint.mission.discodeit.domain.user.service;

import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.domain.user.service.basic.UserBinaryContentService;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.core.exception.SdkClientException;

class UserBinaryContentServiceTest extends IntegrationTestSupport {

  @Autowired
  private UserBinaryContentService userBinaryContentService;
  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @MockitoBean
  private BinaryContentStorage binaryContentStorage;

  @BeforeEach
  void setUp() {
    binaryContentRepository.deleteAllInBatch();
  }

  @DisplayName("바이너리 컨텐츠를 업로드하는데 성공하면, 컨텐츠 상태를 성공으로 기록합니다.")
  @Test
  void createBinaryContent_Success() {
    // given
    BinaryContentRequest binaryContentRequest = new BinaryContentRequest("file", "type", 0,
        "hello".getBytes());
    BDDMockito.given(binaryContentStorage.put(any(UUID.class), any()))
        .willReturn(CompletableFuture.completedFuture(UUID.randomUUID()));

    // when
    BinaryContent binaryContent = userBinaryContentService.createBinaryContent(
        binaryContentRequest);

    // then
    Assertions.assertThat(binaryContent.getBinaryContentUploadStatus())
        .isEqualTo(BinaryContentUploadStatus.SUCCESS);
  }

  @DisplayName("바이너리 컨텐츠를 업로드하는데 실패하면, 컨텐츠 상태를 실패로 기록합니다.")
  @Test
  void createBinaryContent_Failed() {
    // given
    BinaryContentRequest binaryContentRequest = new BinaryContentRequest("file", "type", 0,
        "hello".getBytes());
    BDDMockito.given(binaryContentStorage.put(any(UUID.class), any()))
        .willReturn(CompletableFuture.failedFuture(SdkClientException.builder().build()));

    // when
    BinaryContent binaryContent = userBinaryContentService.createBinaryContent(
        binaryContentRequest);

    // then
    Assertions.assertThat(binaryContent.getBinaryContentUploadStatus())
        .isEqualTo(BinaryContentUploadStatus.FAILED);
  }

}