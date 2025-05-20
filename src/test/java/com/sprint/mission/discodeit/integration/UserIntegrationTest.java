package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("POST /api/users - 사용자 생성")
    void createUser_success_multipart() throws Exception {
        // given
        CreateUserRequest request = new CreateUserRequest("user", "password",
            "test@example.com", null, null);

        MockMultipartFile jsonPart = new MockMultipartFile("userCreateRequest", "",
            "application/json", objectMapper.writeValueAsBytes(request));

        // when & then
        mockMvc.perform(multipart("/api/users")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("user"))
            .andExpect(jsonPath("$.email").value("test@example.com"));

        assertThat(userRepository.findAll())
            .anyMatch(user -> user.getUsername().equals("user"));
    }

    @Test
    @DisplayName("POST /api/users - 사용자 생성 (중복)")
    void createUser_fail_duplicateUser() throws Exception {
        // given
        User duplicatedUser = new User("user", "password",
            "test@example.com", null);
        userRepository.save(duplicatedUser);

        CreateUserRequest request = new CreateUserRequest("user", "password123!",
            "test@example.com", null, null);

        MockMultipartFile jsonPart = new MockMultipartFile("userCreateRequest", "",
            "application/json", objectMapper.writeValueAsBytes(request));

        // when & then
        mockMvc.perform(multipart("/api/users")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("DUPLICATE_USER"))
            .andExpect(jsonPath("$.exceptionType").value("DuplicateUserException"));
    }

    @Test
    @DisplayName("PATCH /api/users/{id} - 사용자 수정")
    void updateUser_success() throws Exception {
        // given
        User user = new User("beforeUser", "password", "before@example.com", null);
        User savedUser = userRepository.save(user);

        UpdateUserRequest updateRequest = new UpdateUserRequest(savedUser.getId(), "afterUser",
            "newPassword", "after@example.com");

        MockMultipartFile jsonPart = new MockMultipartFile("userUpdateRequest", "",
            "application/json", objectMapper.writeValueAsBytes(updateRequest));

        // when & then
        mockMvc.perform(multipart("/api/users/" + savedUser.getId())
                .file(jsonPart)
                .with(req -> {
                    req.setMethod("PATCH");
                    return req;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("afterUser"))
            .andExpect(jsonPath("$.email").value("after@example.com"));

        User updated = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(updated.getUsername()).isEqualTo("afterUser");
        assertThat(updated.getEmail()).isEqualTo("after@example.com");
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - 사용자 삭제")
    void deleteUser_success() throws Exception {
        // given
        User user = new User("user", "password", "test@example.com", null);
        User savedUser = userRepository.save(user);

        // when & then
        mockMvc.perform(delete("/api/users/" + savedUser.getId()))
            .andExpect(status().isNoContent());

        assertThat(userRepository.findById(savedUser.getId())).isEmpty();
    }

    @Test
    @DisplayName("GET /api/users - 사용자 목록 조회")
    void getAllUsers_success() throws Exception {
        // given
        User user1 = new User("user1", "pw1", "user1@example.com", null);
        User user2 = new User("user2", "pw2", "user2@example.com", null);
        userRepository.saveAll(List.of(user1, user2));

        // when & then
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].username").exists())
            .andExpect(jsonPath("$[0].email").exists())
            .andExpect(jsonPath("$[1].username").exists())
            .andExpect(jsonPath("$[1].email").exists());
    }

}
