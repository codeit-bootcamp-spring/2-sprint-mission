package com.sprint.mission.discodeit.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private UserService userService;


    @BeforeEach
    void cleanUp() {
        userJPARepository.deleteAll();  // 모든 유저 삭제
    }

    @Test
    @DisplayName("[User][통합] User 기본 생성/조회/수정/삭제 테스트")
    void testUser() throws Exception {
        // User 정보 생성
        MockMultipartFile requestPart = new MockMultipartFile(
                "userCreateRequest",
                null,
                "application/json",
                objectMapper.writeValueAsBytes(new UserCreateDto("호날두", "ronaldo@naver.com", "54321"))
        );
        // User 정보 API 전달
        mockMvc.perform(multipart("/api/users").file(requestPart).contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        // 생성된 User 확인
        List<UserResponseDto> users = userService.findAllUser();
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).username()).isEqualTo("호날두");

        // User update 정보 생성
        MockMultipartFile requestUpdate = new MockMultipartFile(
                "userUpdateRequest",
                null,
                "application/json",
                objectMapper.writeValueAsBytes(new UserUpdateDto("손흥민", "sonny@naver.com", "12345"))
        );

        // 기존 User ID 조회
        UserResponseDto user = userService.findAllUser().stream()
                .filter(u -> u.username().equals("호날두"))
                .findFirst()
                .orElse(null);
        UUID matchUserId = Objects.requireNonNull(user).id();

        // User update 정보 API 전달
        mockMvc.perform(multipart("/api/users/{userId}", matchUserId)
                        .file(requestUpdate)
                        .with(request -> {request.setMethod("PATCH"); return request;})
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                        .andExpect(status().isOk());

        // 수정 된 User 정보 확인
        List<UserResponseDto> updateUsers = userService.findAllUser();
        assertThat(updateUsers).isNotEmpty();
        assertThat(updateUsers.get(0).username()).isEqualTo("손흥민");
        assertThat(updateUsers.get(0).email()).isEqualTo("sonny@naver.com");

        // 생성된 User 삭제
        mockMvc.perform(delete("/api/users/{id}", matchUserId)).andExpect(status().isNoContent());
    }
}
