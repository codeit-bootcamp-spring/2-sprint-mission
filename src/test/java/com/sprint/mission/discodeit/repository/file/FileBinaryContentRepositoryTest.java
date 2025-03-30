package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;

import static com.sprint.mission.discodeit.constant.MessageInfo.MESSAGE_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;


class FileBinaryContentRepositoryTest {
    @TempDir
    private Path path;

    private BinaryContentRepository binaryContentRepository;

    @BeforeEach
    void setUp() {
        binaryContentRepository = new FileBinaryContentRepository(path.resolve("binaryContent.ser"));
    }

    @Test
    void save() {
        MockMultipartFile file = new MockMultipartFile(MediaType.IMAGE_JPEG_VALUE, MESSAGE_CONTENT.getBytes());

        BinaryContent binaryContent = new BinaryContent(file);
        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        assertThat(binaryContent.getId()).isEqualTo(savedBinaryContent.getId());
    }
}