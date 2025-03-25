package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class FileBinaryContentRepositoryTest {

    private FileBinaryContentRepository binaryContentRepository;

    @BeforeEach
    void setUp() {
        binaryContentRepository = new FileBinaryContentRepository();
    }

    @Test
    void save() {
        BinaryContent binaryContent = new BinaryContent(Paths.get(UUID.randomUUID().toString()));
        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        assertThat(binaryContent.getId()).isEqualTo(savedBinaryContent.getId());
    }
}