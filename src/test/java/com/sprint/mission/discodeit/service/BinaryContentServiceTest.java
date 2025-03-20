package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.BinaryContentDto;
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
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class BinaryContentServiceTest {
    private BinaryContentService binaryContentService;

    @BeforeEach
    void setUp() {
        BinaryContentRepository binaryContentRepository = new JCFBinaryContentRepository();
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
    }

    @DisplayName("이미지 파일 저장 후 경로 반환 테스트")
    @Test
    void createProfileImage() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                MESSAGE_CONTENT.getBytes()
        );

        UUID profileId = binaryContentService.createProfileImage(file);
        BinaryContentDto binaryContent = binaryContentService.findById(profileId);

        byte[] storedFileBytes = Files.readAllBytes(binaryContent.path());
        assertThat(file.getBytes()).isEqualTo(storedFileBytes);
    }

    @DisplayName("기존 이미지 삭제시 로컬 저장소에도 삭제됩니다.")
    @Test
    void updateProfileImage() {
        MockMultipartFile file = new MockMultipartFile(
                MediaType.IMAGE_JPEG_VALUE,
                MESSAGE_CONTENT.getBytes()
        );

        UUID profileId = binaryContentService.createProfileImage(file);
        BinaryContentDto binaryContent = binaryContentService.findById(profileId);
        binaryContentService.delete(profileId);

        assertAll(
                () -> assertThatThrownBy(() -> binaryContentService.findById(profileId))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThat(Files.exists(binaryContent.path())).isFalse()
        );
    }
}