package com.sprint.mission.discodeit.repository.jcf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JCFUserStatusRepositoryTest {

    private JCFUserStatusRepository userStatusRepository;

    @BeforeEach
    void setUp() {
        userStatusRepository = new JCFUserStatusRepository();
    }

    @Test
    @DisplayName("findByUserId로 userStatus 찾기 .")
    void findByUserIdTest() {
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(userId, now);

        userStatusRepository.save(userStatus);

        // when
        UserStatus result = userStatusRepository.findByUserId(userId);

        // then
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }
}
