package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.READ_STATUS_TEST_FILE;
import static org.assertj.core.api.Assertions.assertThat;

class FileReadStatusRepositoryTest {

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(READ_STATUS_TEST_FILE);
    }

    @Test
    void save() {
        ReadStatusRepository readStatusRepository = new FileReadStatusRepository();
        ReadStatus readStatus = new ReadStatus(UUID.randomUUID(), UUID.randomUUID());
        ReadStatus saveReadStatus = readStatusRepository.save(readStatus);

        assertThat(readStatus.getId()).isEqualTo(saveReadStatus.getId());
    }
}