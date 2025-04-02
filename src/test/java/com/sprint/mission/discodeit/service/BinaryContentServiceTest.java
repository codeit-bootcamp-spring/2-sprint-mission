package com.sprint.mission.discodeit.service;

import static com.sprint.mission.discodeit.util.mock.file.MockFile.createMockImageFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class BinaryContentServiceTest {

  private BinaryContentService binaryContentService;

  @BeforeEach
  void setUp() {
    binaryContentService = new BasicBinaryContentService(new JCFBinaryContentRepository());
  }

  @DisplayName("이미지 파일을 저장하면 올바른 경로가 반환된다.")
  @Test
  void createProfileImage() {
    MultipartFile file = createMockImageFile("dog.jpg");
    UUID profileId = binaryContentService.createProfileImage(file);
    BinaryContentResult binaryContent = binaryContentService.getById(profileId);

    assertThat(profileId).isEqualTo(binaryContent.id());
  }

  @DisplayName("여러 ID로 조회하면 해당하는 모든 바이너리 콘텐츠가 반환된다.")
  @Test
  void findAllByIdIn() {
    UUID profileId1 = binaryContentService.createProfileImage(
        createMockImageFile("Kirby.jpg"));
    UUID profileId2 = binaryContentService.createProfileImage(createMockImageFile("dog.jpg"));
    List<BinaryContentResult> binaryContentsResults = binaryContentService.getByIdIn(
        List.of(profileId1, profileId2));

    BinaryContentResult binaryContentResult1 = binaryContentsResults.get(0);
    BinaryContentResult binaryContentResult2 = binaryContentsResults.get(1);

    assertAll(
        () -> assertThat(binaryContentResult1.id()).isEqualTo(profileId1),
        () -> assertThat(binaryContentResult2.id()).isEqualTo(profileId2)
    );
  }

  @DisplayName("이미지를 삭제하면 로컬 저장소에서도 삭제된다.")
  @Test
  void deleteProfileImage() {
    MultipartFile file = createMockImageFile("dog.jpg");
    UUID profileId = binaryContentService.createProfileImage(file);

    binaryContentService.delete(profileId);

    assertThatThrownBy(() -> binaryContentService.getById(profileId))
        .isInstanceOf(IllegalArgumentException.class);
  }
}