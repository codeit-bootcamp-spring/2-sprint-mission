package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.BINARY_CONTENT_TEST_FILE;
import static org.assertj.core.api.Assertions.assertThat;

class FileBinaryContentRepositoryTest {

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(BINARY_CONTENT_TEST_FILE);
    }

    @Test
    void save() {
        BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository();
        BinaryContent binaryContent = new BinaryContent(Paths.get(UUID.randomUUID().toString()));
        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        assertThat(binaryContent.getId()).isEqualTo(savedBinaryContent.getId());
    }
}