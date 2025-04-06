package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.application.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusResult;
import com.sprint.mission.discodeit.application.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.JsonConvertor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.util.mock.file.FileInfo.IMAGE_NAME_DOG;
import static com.sprint.mission.discodeit.util.mock.file.MockFile.createMockImageFile;
import static com.sprint.mission.discodeit.util.mock.user.UserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.util.mock.user.UserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvcTester mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserStatusService userStatusService;

    @Test
    void register() {
        User user = new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null);
        UserResult stubResult = UserResult.fromEntity(user, false);
        when(userService.register(any(), any())).thenReturn(stubResult);

        assertThat(mockMvc.post()
                .uri("/api/users")
                .multipart()
                .file(new MockMultipartFile("userCreateRequest", null, MediaType.APPLICATION_JSON_VALUE,
                        JsonConvertor.asString(new UserCreateRequest(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword())).getBytes()))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(user.getId().toString());
    }

    @Test
    void getById() {
        User user = new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null);
        UserResult stubResult = UserResult.fromEntity(user, false);

        when(userService.getById(any())).thenReturn(stubResult);

        assertThat(mockMvc.get()
                .uri("/api/users/{authorId}", user.getId())
                .queryParam("authorId", user.getId().toString()))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.id")
                .isEqualTo(user.getId().toString());
    }

    @Test
    void getAll() {
        User user = new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null);
        UserResult stubResult = UserResult.fromEntity(user, false);
        User otherUser = new User(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword(), null);
        UserResult otherUserStubResult = UserResult.fromEntity(otherUser, false);

        when(userService.getAll()).thenReturn(List.of(stubResult, otherUserStubResult));

        assertThat(mockMvc.get()
                .uri("/api/users"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].id")
                .isEqualTo(List.of(user.getId().toString(), otherUser.getId().toString()));
    }

    @Test
    void updateUserName() {
        User user = new User(OTHER_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null);
        UserResult stubResult = UserResult.fromEntity(user, false);

        when(userService.update(any(), any(), any())).thenReturn(stubResult);

        assertThat(mockMvc.patch()
                .uri("/api/users/{userId}", user.getId())
                .multipart()
                .file(new MockMultipartFile("userUpdateRequest", null, MediaType.APPLICATION_JSON_VALUE,
                        JsonConvertor.asString(new UserUpdateRequest(OTHER_USER.getName(), null, null)).getBytes()))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.username")
                .isEqualTo(OTHER_USER.getName());
    }

    @Test
    void updateProfileImage() throws IOException {
        UUID profileId = UUID.randomUUID();
        User user = new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), profileId);
        UserResult stubResult = UserResult.fromEntity(user, false);

        when(userService.update(any(), any(), any())).thenReturn(stubResult);

        assertThat(mockMvc.patch()
                .uri("/api/users/{userId}", user.getId())
                .multipart()
                .file(new MockMultipartFile("userUpdateRequest", null, MediaType.APPLICATION_JSON_VALUE,
                        JsonConvertor.asString(new UserUpdateRequest(null, null, null)).getBytes()))
                .file("profileImage", createMockImageFile(IMAGE_NAME_DOG).getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.profileId")
                .isEqualTo(profileId.toString());
    }

    @Test
    void updateOnlineStatus() {
        User user = new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), UUID.randomUUID());
        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        UserStatusResult stubResult = UserStatusResult.fromEntity(userStatus, true);

        when(userStatusService.updateByUserId(any(), any())).thenReturn(stubResult);

        assertThat(mockMvc.patch()
                .uri("/api/users/{userId}/userStatus", user.getId())
                .content(JsonConvertor.asString(new UserStatusUpdateRequest(Instant.now())))
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.online")
                .isEqualTo(true);
    }

    @Test
    void delete() {
        UUID userId = UUID.randomUUID();
        assertThat(mockMvc.delete().uri("/api/users/{authorId}", userId))
                .hasStatus(HttpStatus.NO_CONTENT);
    }
}