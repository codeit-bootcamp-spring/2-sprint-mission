package com.sprint.mission.discodeit.domain.binarycontent.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.domain.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class BasicBinaryContentServiceTest extends IntegrationTestSupport {

  @Autowired
  private BinaryContentRepository binaryContentRepository;
  @Autowired
  private BinaryContentService binaryContentService;

  @MockitoBean
  private BinaryContentStorage binaryContentStorage;

  @BeforeEach
  void beforeEach() {
    binaryContentRepository.deleteAllInBatch();
  }

  @DisplayName("바이너리 컨텐츠를 생성합니다.")
  @Test
  void createBinaryContent() {
    // given
    String name = UUID.randomUUID().toString();
    BinaryContentRequest binaryContentRequest = new BinaryContentRequest(name, "", 0,
        "hello".getBytes());

    // when
    BinaryContentResult binaryContent = binaryContentService.createBinaryContent(
        binaryContentRequest);

    // then
    Assertions.assertThat(binaryContent.name()).isEqualTo(name);

    verify(binaryContentStorage, times(1)).put(any(), any());
  }

  @DisplayName("null 받을 경우, null을 리턴합니다.")
  @Test
  void createBinaryContent_Null() {
    // when
    BinaryContentResult binaryContent = binaryContentService.createBinaryContent(null);

    // then
    Assertions.assertThat(binaryContent).isNull();
  }


  @DisplayName("ID로 조회하면, 해당 객체를 반환한다.")
  @Test
  void getById() {
    // given
    BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent("", "", 0));

    // when
    BinaryContentResult binaryContentResult = binaryContentService.getById(binaryContent.getId());

    // then
    Assertions.assertThat(binaryContentResult.id()).isEqualTo(binaryContent.getId());
  }

  @DisplayName("ID로 조회하면, 해당 객체를 반환한다.")
  @Test
  void getById_NoException() {
    // when & then
    Assertions.assertThatThrownBy(() -> binaryContentService.getById(UUID.randomUUID()))
        .isInstanceOf(BinaryContentNotFoundException.class);
  }

  @DisplayName("여러개의 ID로 조회하면, 해당 객체를 반환한다.")
  @Test
  void getByIdIn() {
    // given
    BinaryContent firstBinaryContent = binaryContentRepository.save(new BinaryContent("", "", 0));
    BinaryContent secondBinaryContent = binaryContentRepository.save(new BinaryContent("", "", 0));

    // when
    List<BinaryContentResult> binaryContentResults = binaryContentService.getByIdIn(
        List.of(firstBinaryContent.getId(), secondBinaryContent.getId()));

    // then
    Assertions.assertThat(binaryContentResults)
        .extracting(BinaryContentResult::id)
        .containsExactlyInAnyOrder(firstBinaryContent.getId(), secondBinaryContent.getId());
  }

  @DisplayName("데이터에 저장된 바이너리 컨텐츠를 삭제합니다.")
  @Test
  void delete() {
    // given
    BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent("", "", 10));

    // when
    binaryContentService.delete(binaryContent.getId());

    // then
    Assertions.assertThat(binaryContentRepository.findById(binaryContent.getId())).isNotPresent();
  }

  @DisplayName("바이너리 컨텐츠를 삭제시, 해당 객체가 없으면 삭제합니다.")
  @Test
  void delete_NoException() {
    // when & then
    Assertions.assertThatThrownBy(() -> binaryContentService.delete(UUID.randomUUID()))
        .isInstanceOf(BinaryContentNotFoundException.class);
  }

}