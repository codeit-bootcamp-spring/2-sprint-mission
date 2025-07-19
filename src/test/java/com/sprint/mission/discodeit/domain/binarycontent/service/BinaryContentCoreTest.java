package com.sprint.mission.discodeit.domain.binarycontent.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.common.s3.S3Adapter;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.service.basic.BinaryContentCore;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Disabled // 추후 수정예정입니다.
class BinaryContentCoreTest extends IntegrationTestSupport {

  @Autowired
  private BinaryContentCore binaryContentCore;
  @Autowired
  private BinaryContentRepository binaryContentRepository;
  @Autowired
  private BinaryContentStorage binaryContentStorage;
  @MockitoBean
  private S3Adapter s3Adapter;

  @AfterEach
  void tearDown() {
    binaryContentRepository.deleteAllInBatch();
  }

  @DisplayName("바이너리 컨텐츠를 생성합니다.(아마 s3일떄만 통과)")
  @Test
  void createBinaryContent() {
    // given
    String name = UUID.randomUUID().toString();
    BinaryContentRequest binaryContentRequest = new BinaryContentRequest(name, "", 0,
        "hello".getBytes());

    // when
    BinaryContent binaryContent = binaryContentCore.createBinaryContent(binaryContentRequest);

    // then
    Assertions.assertThat(binaryContent.getFileName()).isEqualTo(name);

    verify(binaryContentStorage, times(1)).put(any(), any());
  }


  @DisplayName("null 받을 경우, null을 리턴합니다.")
  @Test
  void createBinaryContent_Null() {
    // when
    BinaryContent binaryContent = binaryContentCore.createBinaryContent(null);

    // then
    Assertions.assertThat(binaryContent).isNull();
  }

  @DisplayName("여러번 바이너리 컨텐츠를 생성합니다.")
  @Test
  void createBinaryContents() {
    // given
    String firstName = UUID.randomUUID().toString();
    String secondName = UUID.randomUUID().toString();
    BinaryContentRequest firstBinaryContentRequest = new BinaryContentRequest(firstName, "", 0,
        "hello".getBytes());
    BinaryContentRequest secondBinaryContentRequest = new BinaryContentRequest(secondName, "", 0,
        "hello".getBytes());
    List<BinaryContentRequest> binaryContentRequests = List.of(firstBinaryContentRequest,
        secondBinaryContentRequest);

    // when
    List<BinaryContent> binaryContents = binaryContentCore.createBinaryContents(
        binaryContentRequests);

    // then
    Assertions.assertThat(binaryContents)
        .extracting(BinaryContent::getFileName)
        .containsExactlyInAnyOrder(firstName, secondName);
  }

  @DisplayName("데이터에 저장된 바이너리 컨텐츠를 삭제합니다.")
  @Test
  void delete() {
    // given
    BinaryContentRequest binaryContentRequest = new BinaryContentRequest("", "", 0,
        "hello".getBytes());
    BinaryContent binaryContent = binaryContentCore.createBinaryContent(binaryContentRequest);

    // when
    binaryContentCore.delete(binaryContent.getId());

    // then
    Assertions.assertThat(binaryContentRepository.findById(binaryContent.getId())).isNotPresent();
  }

  @DisplayName("바이너리 컨텐츠를 삭제시, 해당 객체가 없으면 삭제합니다.")
  @Test
  void delete_NoException() {
    // when & then
    Assertions.assertThatThrownBy(() -> binaryContentCore.delete(UUID.randomUUID()))
        .isInstanceOf(BinaryContentNotFoundException.class);
  }

}