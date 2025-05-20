package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestJpaConfig;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.saveAll(List.of(
            new User("test1", "pw2", "test1@email.com", null),
            new User("test2", "pw2", "test2@email.com", null),
            new User("test3", "pw2", "test3@email.com", null)
        ));
    }

    @Test
    @DisplayName("findByUsername(): 존재하는 사용자명을 조회 할 때 Optional에 결과가 담김")
    void findByUsername_whenUserExists_shouldReturnUser() {
        // when
        Optional<User> result = userRepository.findByUsername("test1");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test1@email.com");
    }

    @Test
    @DisplayName("findByUsername(): 존재하지 않은 사용자명을 조회하면 Optional.empty()를 반환")
    void findByUsername_whenUserDoesNotExist_shouldReturnEmpty() {
        // when
        Optional<User> result = userRepository.findByUsername("nonexistent");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll(): 페이징 처리 시 지정한 사이즈 만큼 결과를 반환한다.")
    void findAll_withPaging_shouldReturnLimitedResults() {
        // when
        Page<User> page = userRepository.findAll(PageRequest.of(0, 2));

        // then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("findAll(): username 내림차순 정렬 적용")
    void findAll_withSorting_shouldReturnInDescendingOrder() {
        // when
        Page<User> page = userRepository.findAll(
            PageRequest.of(0, 3, Sort.by("username").descending()));

        // then
        List<User> users = page.getContent();
        assertThat(users).hasSize(3);
        assertThat(users.get(0).getUsername()).isEqualTo("test3");
        assertThat(users.get(1).getUsername()).isEqualTo("test2");
        assertThat(users.get(2).getUsername()).isEqualTo("test1");

    }
}
