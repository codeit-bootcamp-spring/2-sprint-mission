package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.util.mock.user.UserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.util.mock.user.UserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;

class FileUserRepositoryTest {

    private static final String USER_FILE = "user.ser";
    @TempDir
    private Path path;

    private UserRepository userRepository;
    private User setUpUser;
    private User savedSetUpUser;

    @BeforeEach
    void setUp() {
        userRepository = new FileUserRepository(path.resolve(USER_FILE));
        setUpUser = new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(),
                null);
        savedSetUpUser = userRepository.save(setUpUser);
    }

    @DisplayName("유저를 저장할 경우, 저장한 유저를 반환합니다.")
    @Test
    void save() {
        assertThat(setUpUser.getId()).isEqualTo(savedSetUpUser.getId());
    }

    @DisplayName("유저 ID로 조회할 경우, 같은 ID를 가진 유저를 반환합니다.")
    @Test
    void findByUserId() {
        Optional<User> user = userRepository.findByUserId(savedSetUpUser.getId());

        assertThat(user).map(User::getId)
                .hasValue(savedSetUpUser.getId());
    }

    @DisplayName("유저 이름으로 조회할 경우, 같은 이름을 가진 유저를 반환합니다.")
    @Test
    void findByName() {
        Optional<User> user = userRepository.findByName(LOGIN_USER.getName());

        assertThat(user).map(User::getId)
                .hasValue(savedSetUpUser.getId());
    }

    @DisplayName("유저 이메일로 조회할 경우, 같은 이메일를 가진 유저를 반환합니다.")
    @Test
    void findByEmail() {
        Optional<User> user = userRepository.findByEmail(LOGIN_USER.getEmail());

        assertThat(user).map(User::getId)
                .hasValue(savedSetUpUser.getId());
    }

    @DisplayName("전체 유저를 조회할 경우, 등록된 유저 전체를 반환합니다.")
    @Test
    void findAll() {
        User otherUser = userRepository.save(
                new User(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword(), null));
        List<User> users = userRepository.findAll();

        assertThat(users).extracting(User::getId)
                .containsExactlyInAnyOrder(otherUser.getId(), savedSetUpUser.getId());
    }

    @DisplayName("유저를 삭제할 경우, 반환값은 없습니다.")
    @Test
    void delete() {
        userRepository.delete(savedSetUpUser.getId());
        Optional<User> user = userRepository.findByUserId(savedSetUpUser.getId());

        assertThat(user).isNotPresent();
    }
}