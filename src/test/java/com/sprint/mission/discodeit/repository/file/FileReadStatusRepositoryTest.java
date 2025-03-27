package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FileReadStatusRepositoryTest {
    @TempDir
    private Path path;

    @Test
    void save() {
        ReadStatusRepository readStatusRepository = new FileReadStatusRepository(path.resolve("readStatus.ser"));
        ReadStatus readStatus = new ReadStatus(UUID.randomUUID(), UUID.randomUUID());
        ReadStatus saveReadStatus = readStatusRepository.save(readStatus);

        assertThat(readStatus.getId()).isEqualTo(saveReadStatus.getId());
    }
}