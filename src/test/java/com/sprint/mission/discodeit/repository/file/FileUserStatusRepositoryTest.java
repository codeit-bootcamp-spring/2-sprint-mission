package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.FilePath.USER_STATUS_TEST_FILE;
import static org.assertj.core.api.Assertions.assertThat;

class FileUserStatusRepositoryTest {
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(USER_STATUS_TEST_FILE);
    }

    @Test
    void update() {
        UserStatusRepository userStatusRepository = new FileUserStatusRepository();
        UserStatus userStatus = new UserStatus(UUID.randomUUID());
        Instant beforeLastLogin = userStatus.getLastLoginAt();
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);
        savedUserStatus.updateLastLoginAt();

        assertThat(savedUserStatus.getLastLoginAt()).isAfter(beforeLastLogin);
    }
}