package com.sprint.mission.discodeit.serviceTest;

import com.sprint.mission.discodeit.jcf.JCFUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private JCFUserService userService;

    @BeforeEach
    void setUp() {
        userService = JCFUserService.getInstance();
    }

    @Test
    @DisplayName("유저 생성 확인")
    void testCreateUser() {
        userService.createUser("testUser");
        assertNotNull(userService.getUserByName("testUser"));
    }

    @Test
    @DisplayName("유저 조회 실패 확인")
    void testUserNotFound() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByName("unknown"));
    }
}
