package com.sprint.mission.discodeit.domain.message.service.basic;

import static org.mockito.ArgumentMatchers.any;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class MessageBinaryContentServiceTest extends IntegrationTestSupport {

  @Autowired
  private BinaryContentRepository binaryContentRepository;
  @Autowired
  private MessageBinaryContentService messageBinaryContentService;

  @MockitoBean
  private ApplicationEventPublisher eventPublisher;
  @MockitoBean
  private BinaryContentStorage binaryContentStorage;

  @BeforeEach
  void setUp() {
    binaryContentRepository.deleteAllInBatch();
  }

  @DisplayName("첨부파일들을 올리면 바이너리 스토리지에 저장합니다.")
  @Test
  void createBinaryContents() {
    // given
    String firstFileName = UUID.randomUUID().toString();
    String secondFileName = UUID.randomUUID().toString();
    BinaryContentRequest firstRequest = new BinaryContentRequest(firstFileName, "", 0,
        "Hello".getBytes());
    BinaryContentRequest secondRequest = new BinaryContentRequest(secondFileName, "", 0,
        "Hello".getBytes());
    List<BinaryContentRequest> contentRequests = List.of(firstRequest, secondRequest);

    BDDMockito.given(binaryContentStorage.put(any(), any()))
        .willReturn(CompletableFuture.completedFuture(UUID.randomUUID()));

    // when
    List<BinaryContent> binaryContents = messageBinaryContentService.createBinaryContents(
        contentRequests);

    // then
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(binaryContents)
          .extracting(BinaryContent::getId)
          .isNotNull();
      softly.assertThat(binaryContents)
          .extracting(BinaryContent::getBinaryContentUploadStatus)
          .containsExactly(BinaryContentUploadStatus.SUCCESS, BinaryContentUploadStatus.SUCCESS);
      softly.assertThat(binaryContents)
          .extracting(BinaryContent::getFileName)
          .containsExactly(firstFileName, secondFileName);
    });
  }

}