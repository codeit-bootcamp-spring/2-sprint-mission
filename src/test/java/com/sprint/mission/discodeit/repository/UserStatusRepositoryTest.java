package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class UserStatusRepositoryTest {

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        userStatusRepository.deleteAllInBatch();

        user = new User("user1", "pw", "user@test.com", null);
        userRepository.save(user);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void 사용자_상태_사용자_아이디_조회_테스트() {
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElseThrow();

        assertThat(userStatus.getUser().getId()).isEqualTo(user.getId());
    }
}