package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Autowired
    ReadStatusRepository readStatusRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User firstUser;
    private User secondeUser;
    private BinaryContent profile;

    @BeforeEach
    void setUp() {
        // 모든 데이터 삭제
        userRepository.deleteAllInBatch();
        binaryContentRepository.deleteAllInBatch();
        readStatusRepository.deleteAllInBatch();

        // 테스트 데이터 생성
        profile = new BinaryContent("테스트", 10L, "image/jpeg");

        firstUser = new User("user1", "pw1", "user1@test.com", profile);
        userRepository.save(firstUser);

        secondeUser = new User("user2", "pw2", "user2@test.com", null);
        userRepository.save(secondeUser);

        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 모든_유저_조회_테스트() {
        List<User> users = userRepository.findAllWithProfileAndStatus();
        assertThat(users).hasSize(2);
    }

    @Test
    void 유저_상태_생성_테스트() {
        List<User> users = userRepository.findAllWithProfileAndStatus(); // fetch join 된 쿼리 사용

        assertThat(users)
            .isNotEmpty()
            .allSatisfy(user -> assertThat(user.getStatus()).isNotNull());
    }

    @Test
    void 유저_상태_유저_아이디_필드_테스트() {
        User user = userRepository.findByUsername("user1").orElseThrow();
        assertThat(user.getStatus().getUser().getId()).isEqualTo(firstUser.getId());
    }

    @Test
    void 유저_이름_조회_테스트() {
        User user = userRepository.findByUsername("user1").orElseThrow();
        assertThat(user.getUsername()).isEqualTo("user1@test.com");
        assertThat(user.getStatus()).isNotNull();
    }

    @Test
    void 유저_이메일_조회_테스트() {
        User user = userRepository.findByEmail("user2@test.com").orElseThrow();
        assertThat(user.getUsername()).isEqualTo("user2");
    }

    @Test
    void 유저_프로필_저장_테스트() {
        User user = userRepository.findByEmail("user1@test.com").orElseThrow();
        assertThat(user.getProfile().getSize()).isEqualTo(10L);
    }

}