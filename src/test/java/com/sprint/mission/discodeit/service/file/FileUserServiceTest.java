package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.config.FilePath.USER_FILE;
import static com.sprint.mission.config.SetUpUserInfo.LONGIN_USER;
import static com.sprint.mission.config.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileUserServiceTest {
    private UserService userService;
    private UserDto setUpUser;

    @BeforeEach
    void setUp() {
        userService = new FileUserService();
        setUpUser = userService.register(
                new UserRegisterDto(LONGIN_USER.getName(), LONGIN_USER.getEmail(), LONGIN_USER.getPassword()));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(USER_FILE.getPath());
    }

    @DisplayName("입력받은 유저정보를 서버에 등록한다")
    @Test
    void register() {
        assertThat(setUpUser.email()).isEqualTo(LONGIN_USER.getEmail());
    }

    @DisplayName("유저 등록시 서버에 이메일이 같은 이미 등록된 유저가 있을떄 예외를 반환한다")
    @Test
    void registerDuplicateUser() {
        UserRegisterDto otherUserWithSameEmail = new UserRegisterDto(OTHER_USER.getName(), LONGIN_USER.getEmail(),
                OTHER_USER.getPassword());

        assertThatThrownBy(() ->
                userService.register(otherUserWithSameEmail))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("id가 같은 통해 유저를 반환한다")
    @Test
    void findById() {
        UserDto user = userService.findById(setUpUser.id());

        assertThat(setUpUser.email())
                .isEqualTo(user.email());
    }

    @DisplayName("찾는 id가 없을이 예외를 반환한다")
    @Test
    void findByIdNoId() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름이 같은 유저들을 반환한다")
    @Test
    void findByName() {
        UserRegisterDto otherUserWithSameEmail = new UserRegisterDto(LONGIN_USER.getName(), OTHER_USER.getEmail(),
                OTHER_USER.getPassword());

        userService.register(otherUserWithSameEmail);
        List<UserDto> users = userService.findByName(setUpUser.name());

        assertThat(users).hasSize(2);
    }

    @DisplayName("이메일이 같은 유저를 반환한다")
    @Test
    void findByEmail() {
        UserDto user = userService.findByEmail(LONGIN_USER.getEmail());

        assertThat(setUpUser.id()).isEqualTo(user.id());
    }

    @DisplayName("이름을 수정하고 유저를 반환한다")
    @Test
    void updateName() {
        String userName = "김철수";
        userService.updateName(setUpUser.id(), userName);

        UserDto updatedUserInfo = userService.findById(setUpUser.id());

        assertThat(setUpUser.name()).isNotEqualTo(updatedUserInfo.name());
    }

    @DisplayName("유저를 삭제한다")
    @Test
    void delete() {
        UUID id = setUpUser.id();
        userService.delete(id);

        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}