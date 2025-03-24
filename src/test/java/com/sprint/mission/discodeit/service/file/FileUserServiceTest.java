package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.application.dto.user.UserDto;
import com.sprint.mission.discodeit.application.dto.user.UserRegisterDto;
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

import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.discodeit.constant.FilePath.USER_TEST_FILE;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileUserServiceTest {
    private Path userPath;
    private UserService userService;
    private FileUserRepository userRepository;
    private UserDto initializedUser;

    @BeforeEach
    void setUp() {
        userPath = STORAGE_DIRECTORY.resolve(USER_TEST_FILE);
        userRepository = new FileUserRepository();
        userRepository.changePath(userPath);
        userService = new FileUserService(userRepository);
        setUpUser();
    }

    private void setUpUser() {
        initializedUser = userService.register(
                new UserRegisterDto(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword()), null);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(userPath);
    }

    @DisplayName("입력받은 유저 정보를 서버에 등록하면 올바르게 저장된다.")
    @Test
    void registerUser() {
        assertThat(initializedUser.email()).isEqualTo(LOGIN_USER.getEmail());
    }

    @DisplayName("이미 등록된 이메일로 유저를 등록하면 예외가 발생한다.")
    @Test
    void registerDuplicateUserThrowsException() {
        UserRegisterDto otherUserWithSameEmail = new UserRegisterDto(OTHER_USER.getName(), LOGIN_USER.getEmail(),
                OTHER_USER.getPassword());

        assertThatThrownBy(() -> userService.register(otherUserWithSameEmail, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ID를 통해 유저를 조회하면 해당 유저를 반환한다.")
    @Test
    void findUserById() {
        UserDto user = userService.findById(initializedUser.id());
        assertThat(initializedUser.email()).isEqualTo(user.email());
    }

    @DisplayName("존재하지 않는 ID로 유저를 조회하면 예외가 발생한다.")
    @Test
    void findByIdThrowsExceptionWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름으로 유저를 조회하면 해당하는 유저를 반환한다.")
    @Test
    void findUserByName() {
        UserRegisterDto otherUser = new UserRegisterDto(LOGIN_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword());
        userService.register(otherUser, null);

        UserDto user = userService.findByName(initializedUser.name());
        assertThat(user.name()).isEqualTo(LOGIN_USER.getName());
    }

    @DisplayName("이메일을 통해 유저를 조회하면 해당하는 유저를 반환한다.")
    @Test
    void findUserByEmail() {
        UserDto user = userService.findByEmail(LOGIN_USER.getEmail());
        assertThat(initializedUser.id()).isEqualTo(user.id());
    }

    @DisplayName("유저의 이름을 수정하면 변경된 정보가 반영된다.")
    @Test
    void updateUserName() {
        String userName = "김철수";
        userService.updateName(initializedUser.id(), userName);

        UserDto updatedUserInfo = userService.findById(initializedUser.id());
        assertThat(initializedUser.name()).isNotEqualTo(updatedUserInfo.name());
    }

    @DisplayName("유저를 삭제하면 조회 시 예외가 발생한다.")
    @Test
    void deleteUser() {
        UUID id = initializedUser.id();
        userService.delete(id);

        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}