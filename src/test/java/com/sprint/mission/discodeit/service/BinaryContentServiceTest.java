package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
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
        BinaryContent binaryContent = binaryContentService.findById(profileId);

        byte[] storedFileBytes = Files.readAllBytes(binaryContent.getPath());
        assertThat(file.getBytes()).isEqualTo(storedFileBytes);
    }
}