package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserStatusService userStatusService;



    @Test
    @DisplayName("[UserController][createUser] User Controller 테스트")
    public void createUser() throws Exception {
        MockMultipartFile requestPart = new MockMultipartFile(
                "userCreateRequest",
                null,
                "application/json",
                objectMapper.writeValueAsBytes(new UserCreateDto("메시", "messi@naver.com", "12345"))
                );

        UUID userId = UUID.randomUUID();
        User saveUser = new User("메시", "messi@naver.com", "12345", null);
        ReflectionTestUtils.setField(saveUser, "id", userId);

        UserResponseDto response = new UserResponseDto(
                saveUser.getId(),
                saveUser.getUsername(),
                saveUser.getEmail(),
                null,
                true
        );

        given(userService.create(any(UserCreateDto.class), any())).willReturn(response);

        mockMvc.perform(multipart("/api/users").file(requestPart).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("메시"))
                .andExpect(jsonPath("$.email").value("messi@naver.com"));
    }

    @Test
    @DisplayName("[UserController][deleteUser] User Controller 테스트")
    public void deleteUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User saveUser = new User("메시", "messi@naver.com", "12345", null);
        ReflectionTestUtils.setField(saveUser, "id", userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("[UserController][findAllUser] User Controller 테스트")
    public void findAllUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User saveUser = new User("메시", "messi@naver.com", "12345", null);
        ReflectionTestUtils.setField(saveUser, "id", userId);

      UserResponseDto response = new UserResponseDto(
                saveUser.getId(),
                saveUser.getUsername(),
                saveUser.getEmail(),
                null,
                true
        );

        given(userService.findAllUser()).willReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId.toString()))
                .andExpect(jsonPath("$[0].username").value("메시"));
    }
}
