package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.sprint.mission.discodeit.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.discodeit.config.SetUpUserInfo.OTHER_USER;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.discodeit.constant.FilePath.USER_TEST_FILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileUserServiceTest {
    private Path userPath;
    private UserService userService;
    private UserDto initializedUser;

    @BeforeEach
    void setUp() {
        setUpTestPath();
        setUpService();
        setUpUser();
    }

    private void setUpService() {
        FileUserRepository userRepository = new FileUserRepository();
        userRepository.changePath(userPath);
        userService = new FileUserService(userRepository);
    }

    private void setUpTestPath() {
        userPath = STORAGE_DIRECTORY.resolve(USER_TEST_FILE);
    }

    private void setUpUser() {
        initializedUser = userService.register(
                new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword()), null);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(userPath);
    }

    @DisplayName("입력받은 유저정보를 서버에 등록한다")
    @Test
    void register() {
        assertThat(initializedUser.email()).isEqualTo(LONGIN_USER.getEmail());
    }

    @DisplayName("유저 등록시 서버에 이메일이 같은 이미 등록된 유저가 있을떄 예외를 반환한다")
    @Test
    void registerDuplicateUser() {
        UserRegisterDto otherUserWithSameEmail = new UserRegisterDto(OTHER_USER.getName(), LONGIN_USER.getEmail(),
                OTHER_USER.getPassword());

        assertThatThrownBy(() ->
                userService.register(otherUserWithSameEmail, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("id가 같은 통해 유저를 반환한다")
    @Test
    void findById() {
        UserDto user = userService.findById(initializedUser.id());

        assertThat(initializedUser.email())
                .isEqualTo(user.email());
    }

    @DisplayName("찾는 id가 없을이 예외를 반환한다")
    @Test
    void findByIdNoId() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름으로 유저를 조회한다")
    @Test
    void findByName() {
        UserRegisterDto otherUserWithSameEmail = new UserRegisterDto(LONGIN_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword());
        userService.register(otherUserWithSameEmail, null);

        UserDto user = userService.findByName(initializedUser.name());

        assertThat(user.name()).isEqualTo(LONGIN_USER.getName());
    }

    @DisplayName("이메일이 같은 유저를 반환한다")
    @Test
    void findByEmail() {
        UserDto user = userService.findByEmail(LONGIN_USER.getEmail());

        assertThat(initializedUser.id()).isEqualTo(user.id());
    }

    @DisplayName("이름을 수정하고 유저를 반환한다")
    @Test
    void updateName() {
        String userName = "김철수";
        userService.updateName(initializedUser.id(), userName);

        UserDto updatedUserInfo = userService.findById(initializedUser.id());

        assertThat(initializedUser.name()).isNotEqualTo(updatedUserInfo.name());
    }

    @DisplayName("유저를 삭제한다")
    @Test
    void delete() {
        UUID id = initializedUser.id();
        userService.delete(id);

        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}