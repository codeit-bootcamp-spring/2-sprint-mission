package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserStatusService userStatusService;


    @Test
    @DisplayName("유저 생성 성공")
    void createUser_success() throws Exception {
        UUID id = UUID.randomUUID();
        UserDto response = new UserDto(id, "tester", "test@email.com", null, true);

        // mock service
        Mockito.when(userService.create(any(), any())).thenReturn(response);

        // JSON Part
        MockMultipartFile userPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                """
                {
                    "username": "tester",
                    "email": "test@email.com",
                    "password": "1234"
                }
                """.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/api/users")
                        .file(userPart))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("tester"));
    }

    @Test
    @DisplayName("유저 전체 조회 성공")
    void findAllUsers_success() throws Exception {
        UUID id = UUID.randomUUID();
        UserDto u1 = new UserDto(id, "u1", "u1@email.com", null, true);
        Mockito.when(userService.findAll()).thenReturn(List.of(u1));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("u1"));
    }

}
