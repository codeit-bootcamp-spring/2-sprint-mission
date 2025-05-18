package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.exception.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
class BasicBinaryContentServiceTest {

    @Autowired
    private BinaryContentRepository binaryContentRepository;
    @Autowired
    private BinaryContentService binaryContentService;

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
        List<BinaryContentResult> binaryContentResults = binaryContentService.getByIdIn(List.of(firstBinaryContent.getId(), secondBinaryContent.getId()));

        // then
        Assertions.assertThat(binaryContentResults)
                .extracting(BinaryContentResult::id)
                .containsExactlyInAnyOrder(firstBinaryContent.getId(), secondBinaryContent.getId());
    }

}