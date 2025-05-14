package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@Sql(scripts = {"classpath:schema-test.sql"})
public class UserRepositoryTest {

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    @DisplayName("[UserRepository] findByIdWithProfile 조회 테스트")
    public void findByIdWithProfile() {
        User user = new User("메시", "messi@naver.com", "12345", null);

        entityManager.persist(user);
        entityManager.flush();

        Optional<User> result = userJPARepository.findByIdWithProfile(user.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(user.getId());
        assertThat(result.get().getUsername()).isEqualTo("메시");
        assertThat(result.get().getEmail()).isEqualTo("messi@naver.com");
    }

    @Test
    @DisplayName("[UserRepository] 존재하지 않는 사용자 ID로 findByIdWithProfile 조회 테스트")
    public void findByIdWithProfile_Empty() {
        UUID noExistUserId = UUID.randomUUID();

        Optional<User> result = userJPARepository.findByIdWithProfile(noExistUserId);

        assertThat(result).isNotPresent();
    }


    @Test
    @DisplayName("[UserRepository] findAllUsers 조회 테스트")
    public void findAllUsers() {
        User user1 = new User("메시", "messi@naver.com", "12345", null);
        User user2 = new User("호날두", "ronaldo@naver.com", "12345", null);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        List<User> result = userJPARepository.findAllUsers();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(user1.getId());
        assertThat(result.get(0).getUsername()).isEqualTo(user1.getUsername());
        assertThat(result.get(1).getUsername()).isEqualTo("호날두");
    }

}
