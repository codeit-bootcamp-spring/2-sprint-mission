package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.integration.base.BaseIntegrationTest;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.TestMultipartUtil;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class UserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private User setUser;

    @BeforeEach
    public void setup() {
        userRepository.deleteAllInBatch();
        setUser = userRepository.save(new User("setUsername", "setPassword", "set@test.com", null));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 사용자_생성_성공() throws Exception {
        UserCreateRequest request = new UserCreateRequest("username", "abc123@@",
            "user@test.com");
        MockMultipartFile userJson = TestMultipartUtil.jsonPart("userCreateRequest", request);
        MockMultipartFile profile = new MockMultipartFile("profile", "test.jpeg", "image/jpeg",
            "test".getBytes());

        mockMvc.perform(multipart("/api/users")
                .file(userJson)
                .file(profile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value("username"))
            .andExpect(jsonPath("$.email").value("user@test.com"))
            .andExpect(jsonPath("$.profile.fileName").value("test.jpeg"))
            .andExpect(jsonPath("$.profile.contentType").value("image/jpeg"))
            .andDo(print());
    }

    @Test
    void 사용자_생성_이름_중복_저장_실패() throws Exception {
        UserCreateRequest request = new UserCreateRequest("setUsername", "abc123@@",
            "different@test.com");
        MockMultipartFile userJson = TestMultipartUtil.jsonPart("userCreateRequest", request);

        mockMvc.perform(multipart("/api/users")
                .file(userJson)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isConflict())
            .andDo(print());
    }

    @Test
    void 사용자_생성_이메일_중복_저장_실패() throws Exception {
        UserCreateRequest request = new UserCreateRequest("username1", "abc123@@",
            "set@test.com");
        MockMultipartFile userJson = TestMultipartUtil.jsonPart("userCreateRequest", request);

        mockMvc.perform(multipart("/api/users")
                .file(userJson)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isConflict())
            .andDo(print());
    }

    @Test
    void 사용자_수정_성공() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("user", "abc123@@", "user@test.com");
        MockMultipartFile userJson = TestMultipartUtil.jsonPart("userUpdateRequest", request);
        MockMultipartFile profile = new MockMultipartFile("profile", "test.jpeg", "image/jpeg",
            "test".getBytes());

        mockMvc.perform(multipart("/api/users/{userId}", setUser.getId())
                .file(userJson)
                .file(profile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(req -> {
                    req.setMethod("PATCH");
                    return req;
                }))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("user"))
            .andExpect(jsonPath("$.email").value("user@test.com"))
            .andExpect(jsonPath("$.profile.fileName").value("test.jpeg"))
            .andExpect(jsonPath("$.profile.contentType").value("image/jpeg"))
            .andDo(print());
    }

    @Test
    void 사용자_수정_패스워드_검증_실패() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest("user", "abc123@@@@@@@",
            "set@test.com");
        MockMultipartFile userJson = TestMultipartUtil.jsonPart("userUpdateRequest", request);

        mockMvc.perform(multipart("/api/users/{userId}", setUser.getId())
                .file(userJson)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(req -> {
                    req.setMethod("PATCH");
                    return req;
                }))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    void 사용자_삭제_성공() throws Exception {
        mockMvc.perform(delete("/api/users/{userId}", setUser.getId()))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void 사용자_삭제_사용자_조회_실패() throws Exception {
        mockMvc.perform(delete("/api/users/{userId}", UUID.randomUUID()))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    void 사용자_목록_조회() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
