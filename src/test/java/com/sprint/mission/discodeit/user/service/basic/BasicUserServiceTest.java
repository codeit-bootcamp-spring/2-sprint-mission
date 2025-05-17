package com.sprint.mission.discodeit.user.service.basic;

import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest
class BasicUserServiceTest {

    private static final String USER_NAME = "hwang";
    private static final String USER_EMAIL = "h@naver.com";
    private static final String USER_PASSWORD = "1234";

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @AfterEach
    void tearDown() {
        userStatusRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저 등록을 요청하면, 유저와 유저의 상태를 저장한다")
    @Test
    void register() {
        // given
        UserCreateRequest userRequest = new UserCreateRequest(USER_NAME, USER_EMAIL, USER_PASSWORD);

        // when
        UserResult user = userService.register(userRequest, null);

        // then
        assertAll(
                () -> Assertions.assertThat(user)
                        .extracting(UserResult::id)
                        .isEqualTo(user.id()),
                () -> Assertions.assertThat(userStatusRepository.findByUser_Id(user.id())).isPresent()
        );
    }

    @DisplayName("이미 등록된 유저 이름으로 등록 시도시, 예외를 반환한다")
    @Test
    void register_DuplicateNameException() {
        // given
        userRepository.save(new User(USER_NAME, UUID.randomUUID().toString(), UUID.randomUUID().toString(), null));
        UserCreateRequest userRequest = new UserCreateRequest(USER_NAME, UUID.randomUUID().toString(), UUID.randomUUID().toString());

        // when & then
        Assertions.assertThatThrownBy(() -> userService.register(userRequest, null))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("이미 등록된 유저 이름으로 등록 시도시, 예외를 반환한다")
    @Test
    void register_DuplicateEmailException() {
        // given
        userRepository.save(new User(UUID.randomUUID().toString(), USER_EMAIL, UUID.randomUUID().toString(), null));
        UserCreateRequest userRequest = new UserCreateRequest(UUID.randomUUID().toString(), USER_EMAIL, UUID.randomUUID().toString());

        // when & then
        Assertions.assertThatThrownBy(() -> userService.register(userRequest, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("유저 ID로 조회하면, 해당 유저를 반환한다")
    @Test
    void getById() {
        // given
        User savedUser = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));

        // when
        UserResult user = userService.getById(savedUser.getId());

        // then
        Assertions.assertThat(user)
                .extracting(UserResult::id)
                .isEqualTo(savedUser.getId());
    }

    @DisplayName("유저 ID로 조회할때 유저가 없다면, 예외를 반환한다")
    @Test
    void getById_EntityNotFound() {
        // when & then
        Assertions.assertThatThrownBy(() -> userService.getById(UUID.randomUUID()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("유저 이름으로 조회시, 해당 유저를 반환한다.")
    @Test
    void getByName() {
        // given
        userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));

        // when
        UserResult user = userService.getByName(USER_NAME);

        // then
        Assertions.assertThat(user.username())
                .isEqualTo(USER_NAME);
    }


    @DisplayName("유저 이름으로 조회시 해당 이름의 유저가 없다면, 예외를 반환한다.")
    @Test
    void getByName_Exception() {
        // when & then
        Assertions.assertThatThrownBy(() -> userService.getByName(USER_NAME))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("전체 유저를 반환한다.")
    @Test
    void getAllIn() {
        // given
        User saveUser = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));
        User saveAnotherUser = userRepository.save(new User(USER_NAME + 2, USER_EMAIL + 2, USER_PASSWORD, null));

        // when
        List<UserResult> users = userService.getAllIn();

        // then
        Assertions.assertThat(users)
                .extracting(UserResult::id)
                .containsExactlyInAnyOrder(saveUser.getId(), saveAnotherUser.getId());
    }

    @DisplayName("이메일로 조회 시, 유저를 성공적으로 반환한다")
    @Test
    void getByEmail() {
        // given
        User save = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));

        // when
        UserResult user = userService.getByEmail(USER_EMAIL);

        // then
        Assertions.assertThat(user.id()).isEqualTo(save.getId());
    }

    @DisplayName("이메일로 조회 시, 존재하지 않으면 예외를 반환한다")
    @Test
    void getByEmail_EntityNotFound() {
        Assertions.assertThatThrownBy(() -> userService.getByEmail("notfound@example.com"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("업데이트하면, 유저 정보가 수정된다")
    @Test
    void updateUser() {
        // given
        User user = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));
        UUID id = user.getId();

        UserUpdateRequest request = new UserUpdateRequest(
                "newName", "newEmail@example.com", "newPassword");

        // when
        UserResult updatedUser = userService.update(id, request, null);

        // then
        Assertions.assertThat(updatedUser.username()).isEqualTo("newName");
        Assertions.assertThat(updatedUser.email()).isEqualTo("newEmail@example.com");
    }

    @DisplayName("업데이트하면, 유저 정보가 수정된다")
    @Test
    void updateUser_NoUserException() {
        // given
        UserUpdateRequest request = new UserUpdateRequest("newName", "newEmail@example.com", "newPassword");

        // when & then
        Assertions.assertThatThrownBy(() -> userService.update(UUID.randomUUID(), request, null)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("삭제하면, 유저와 유저 상태가 제거된다")
    @Test
    void deleteUser() {
        // given
        User user = userRepository.save(new User(USER_NAME, USER_EMAIL, USER_PASSWORD, null));

        // when
        userService.delete(user.getId());

        // then
        assertAll(
                () -> Assertions.assertThat(userRepository.existsById(user.getId())).isFalse(),
                () -> Assertions.assertThat(userStatusRepository.findByUser_Id(user.getId())).isNotPresent()
        );
    }

    @DisplayName("삭제하려는 유저가 없으면 예외를 던진다")
    @Test
    void deleteUser_NotFound() {
        // when & then
        Assertions.assertThatThrownBy(() -> userService.delete(UUID.randomUUID()))
                .isInstanceOf(NoSuchElementException.class);
    }

}