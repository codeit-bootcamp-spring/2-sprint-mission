package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FileUserStatusRepositoryTest {
    @TempDir
    private Path path;

    @Test
    void update() {
        UserStatusRepository userStatusRepository = new FileUserStatusRepository(path.resolve("userStatus.ser"));
        UserStatus userStatus = new UserStatus(UUID.randomUUID());
        Instant beforeLastLogin = userStatus.getLastLoginAt();
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);
        savedUserStatus.updateLastLoginAt();

        assertThat(savedUserStatus.getLastLoginAt()).isAfter(beforeLastLogin);
    }
}