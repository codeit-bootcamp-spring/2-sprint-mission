package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.dto.binarycontent.BinaryContentResult;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class BinaryContentServiceTest {
    private BinaryContentService binaryContentService;
    private BinaryContentRepository binaryContentRepository;

    @BeforeEach
    void setUp() {
        binaryContentRepository = new JCFBinaryContentRepository();
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
    }

    private MockMultipartFile createMockImageFile(String content) {
        return new MockMultipartFile(MediaType.IMAGE_JPEG_VALUE, content.getBytes());
    }

    @DisplayName("이미지 파일을 저장하면 올바른 경로가 반환된다.")
    @Test
    void createProfileImage() throws IOException {
        MockMultipartFile file = createMockImageFile(MESSAGE_CONTENT);
        UUID profileId = binaryContentService.createProfileImage(file);
        BinaryContentResult binaryContent = binaryContentService.findById(profileId);

        byte[] storedFileBytes = Files.readAllBytes(Path.of(binaryContent.path()));
        assertThat(file.getBytes()).isEqualTo(storedFileBytes);
    }

    @DisplayName("여러 ID로 조회하면 해당하는 모든 바이너리 콘텐츠가 반환된다.")
    @Test
    void findAllByIdIn() {
        MockMultipartFile file1 = createMockImageFile(MESSAGE_CONTENT);
        MockMultipartFile file2 = createMockImageFile("두번째 파일");

        UUID profileId1 = binaryContentService.createProfileImage(file1);
        UUID profileId2 = binaryContentService.createProfileImage(file2);
        List<BinaryContentResult> binaryContentsResults = binaryContentService.findByIdIn(List.of(profileId1, profileId2));

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
        MockMultipartFile file = createMockImageFile(MESSAGE_CONTENT);
        UUID profileId = binaryContentService.createProfileImage(file);
        BinaryContentResult binaryContent = binaryContentService.findById(profileId);

        binaryContentService.delete(profileId);

        assertAll(
                () -> assertThatThrownBy(() -> binaryContentService.findById(profileId))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThat(Files.exists(Path.of(binaryContent.path()))).isFalse()
        );
    }
}