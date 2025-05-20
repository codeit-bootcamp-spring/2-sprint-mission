package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("유저 생성 성공")
    void createUser_success() throws Exception {
        // given
        String username = "tester";
        String email = "test@email.com";
        String password = "1234";
        UserCreateRequest request = new UserCreateRequest(username, email, password);

        // when & then
        mockMvc.perform(
                        multipart("/api/users")
                                .file(new MockMultipartFile(
                                        "userCreateRequest",
                                        "",
                                        "application/json",
                                        new ObjectMapper().writeValueAsBytes(request)
                                ))
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @DisplayName("유저 생성 실패 - 이메일 누락")
    void createUser_fail_noEmail() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest("tester", "", "1234");

        // when & then
        mockMvc.perform(
                        multipart("/api/users")
                                .file(new MockMultipartFile(
                                        "userCreateRequest",
                                        "",
                                        "application/json",
                                        new ObjectMapper().writeValueAsBytes(request)
                                ))
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"));
    }

}
