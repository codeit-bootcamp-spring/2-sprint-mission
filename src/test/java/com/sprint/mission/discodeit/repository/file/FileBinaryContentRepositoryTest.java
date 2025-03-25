package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
        BinaryContent binaryContent = new BinaryContent(Paths.get(UUID.randomUUID().toString()));
        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        assertThat(binaryContent.getId()).isEqualTo(savedBinaryContent.getId());
    }
}