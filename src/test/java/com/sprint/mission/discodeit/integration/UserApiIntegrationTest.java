package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BinaryContentService binaryContentService;

    @MockitoBean
    private BinaryContentRepository binaryContentRepository;

    private static final String DEFAULT_PASSWORD = "123456";
    private static final String TEST_USERNAME = "user";
    private static final String TEST_EMAIL = "user@naver.com";

    // 이미지 생성
    private MockMultipartFile createProfileImage(String filename) {
        return new MockMultipartFile(
            "profileImageFile",
            filename,
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
    }

    private UserCreateRequest createUserRequest(String username, String email) {
        return new UserCreateRequest(username, email, DEFAULT_PASSWORD);
    }

    private UserUpdateRequest createUserUpdateRequest(String username, String email) {
        return new UserUpdateRequest(username, email, DEFAULT_PASSWORD);
    }

    private String createUserAndGetId(String username, String email) throws Exception {
        UserCreateRequest request = createUserRequest(username, email);
        MockMultipartFile requestJson = new MockMultipartFile(
            "userCreateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(request)
        );

        String response = mockMvc.perform(multipart("/api/users")
                .file(requestJson))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        var responseMap = objectMapper.readValue(response, Map.class);
        return responseMap.get("id").toString();
    }

    @Test
    @DisplayName("사용자 생성 성공 통합 테스트")
    @Transactional
    void createUser_Success() throws Exception {
        UserCreateRequest createRequest = createUserRequest(TEST_USERNAME, TEST_EMAIL);
        MockMultipartFile requestJson = new MockMultipartFile(
            "userCreateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(createRequest)
        );
        MockMultipartFile profileImage = createProfileImage("profile.jpg");

        UUID mockAttachmentId = UUID.randomUUID();
        BinaryContentDto mockProfileDto = new BinaryContentDto(
            mockAttachmentId,
            profileImage.getOriginalFilename(),
            profileImage.getSize(),
            profileImage.getContentType()
        );
        when(binaryContentService.create(any(MultipartFile.class))).thenReturn(mockProfileDto);

        BinaryContent mockPersistedProfile =
            new BinaryContent(
                mockProfileDto.contentType(),
                mockProfileDto.fileName(),
                mockProfileDto.size()
            );

        BinaryContent fullyMockedBinaryContent =
            org.mockito.Mockito.mock(BinaryContent.class);
        when(fullyMockedBinaryContent.getId()).thenReturn(mockAttachmentId);
        when(fullyMockedBinaryContent.getFileName()).thenReturn(mockProfileDto.fileName());
        when(fullyMockedBinaryContent.getContentType()).thenReturn(mockProfileDto.contentType());
        when(fullyMockedBinaryContent.getSize()).thenReturn(mockProfileDto.size());

        when(binaryContentRepository.findById(mockAttachmentId)).thenReturn(
            java.util.Optional.of(fullyMockedBinaryContent));

        ResultActions result = mockMvc.perform(multipart("/api/users")
            .file(requestJson)
            .file(profileImage));

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.username").value(TEST_USERNAME))
            .andExpect(jsonPath("$.email").value(TEST_EMAIL));
    }

    @Test
    @DisplayName("사용자 생성 실패 - 중복된 이메일")
    @Transactional
    void createUser_Fail_DuplicateEmail() throws Exception {
        String duplicateEmail = "duplicate@naver.com";

        createUserAndGetId("user1", duplicateEmail);

        //중복
        UserCreateRequest secondUserRequest = createUserRequest("user2", duplicateEmail);
        MockMultipartFile secondRequestJson = new MockMultipartFile(
            "userCreateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(secondUserRequest)
        );

        ResultActions result = mockMvc.perform(multipart("/api/users")
            .file(secondRequestJson));

        result.andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(ErrorCode.USER_ALREADY_EXISTS.name()))
            .andExpect(jsonPath("$.message").value("이미 존재하는 사용자입니다."))
            .andExpect(jsonPath("$.details.email").value(duplicateEmail));
    }

    @Test
    @DisplayName("사용자 생성 실패 - 중복된 사용자명")
    @Transactional
    void createUser_Fail_DuplicateUsername() throws Exception {
        String duplicateUsername = "duplicateUser";
        // 먼저 해당 사용자명으로 사용자를 하나 생성 (이메일은 달라야 함)
        createUserAndGetId(duplicateUsername, "unique1@naver.com");

        UserCreateRequest secondUserRequest = createUserRequest(duplicateUsername,
            "unique2@naver.com");
        MockMultipartFile secondRequestJson = new MockMultipartFile(
            "userCreateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(secondUserRequest)
        );

        ResultActions result = mockMvc.perform(multipart("/api/users")
            .file(secondRequestJson));

        result.andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(ErrorCode.USER_ALREADY_EXISTS.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.USER_ALREADY_EXISTS.getMessage()))
            .andExpect(jsonPath("$.details.username").value(duplicateUsername));
    }

    @Test
    @DisplayName("사용자 생성 실패 - 프로필 이미지 업로드 오류")
    @Transactional
    void createUser_Fail_ProfileImageError() throws Exception {
        UserCreateRequest createRequest = createUserRequest("imageErrorUser", "image@error.com");
        MockMultipartFile requestJson = new MockMultipartFile(
            "userCreateRequest", "", "application/json",
            objectMapper.writeValueAsBytes(createRequest)
        );
        MockMultipartFile profileImage = createProfileImage("error_profile.jpg");

        Map<String, Object> expectedDetails = Map.of("operation", "save-file", "fileName",
            "error_profile.jpg");
        when(binaryContentService.create(any(MultipartFile.class)))
            .thenThrow(new FileProcessingCustomException(expectedDetails));

        ResultActions result = mockMvc.perform(multipart("/api/users")
            .file(requestJson)
            .file(profileImage));

        result.andExpect(status().isInternalServerError()) // GlobalExceptionHandler 설정에 따라 달라질 수 있음
            .andExpect(jsonPath("$.code").value(ErrorCode.FILE_PROCESSING_ERROR.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.FILE_PROCESSING_ERROR.getMessage()))
            .andExpect(jsonPath("$.details.operation").value("save-file"))
            .andExpect(jsonPath("$.details.fileName").value("error_profile.jpg"));
    }

    @Test
    @DisplayName("사용자 목록 조회 통합 테스트")
    @Transactional
    void getAllUsers() throws Exception {
        createUserAndGetId("user1", "user1@naver.com");
        createUserAndGetId("user2", "user2@enaver.com");

        ResultActions result = mockMvc.perform(get("/api/users"));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[?(@.username == 'user1')]").exists())
            .andExpect(jsonPath("$[?(@.username == 'user2')]").exists());
    }

    @Test
    @DisplayName("사용자 수정 성공 통합 테스트")
    @Transactional
    void updateUser_Success() throws Exception {
        String Username = "user";
        String email = "user@naver.com";
        String userId = createUserAndGetId(Username, email);

        String updatedUsername = "updateduser";
        String updatedEmail = "update@naver.com";

        UserUpdateRequest updateRequest = createUserUpdateRequest(updatedUsername, updatedEmail);
        MockMultipartFile updateRequestJson = new MockMultipartFile(
            "userUpdateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(updateRequest)
        );
        MockMultipartFile newProfileImage = createProfileImage("new_profile.png");

        ResultActions updateResult = mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/users/" + userId)
                .file(updateRequestJson)
                .file(newProfileImage));

        updateResult.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId))
            .andExpect(jsonPath("$.username").value(updatedUsername))
            .andExpect(jsonPath("$.email").value(updatedEmail));

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.id == '" + userId + "')].username")
                .value(hasItem(updatedUsername)))
            .andExpect(jsonPath("$[?(@.id == '" + userId + "')].email")
                .value(hasItem(updatedEmail)));
    }

    @Test
    @DisplayName("사용자 수정 실패 - 사용자를 찾을 수 없음")
    @Transactional
    void updateUser_Fail_NotFound() throws Exception {
        String nonExistentUserId = "00000000-0000-0000-0000-000000000000";
        UserUpdateRequest updateRequest = createUserUpdateRequest("updateduser",
            "updatd@naver.com");
        MockMultipartFile updateRequestJson = new MockMultipartFile(
            "userUpdateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(updateRequest)
        );

        ResultActions result = mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/users/" + nonExistentUserId)
                .file(updateRequestJson));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").value("해당 사용자를 찾을 수 없습니다."))
            .andExpect(jsonPath("$.details.userId").value(nonExistentUserId));
    }

    @Test
    @DisplayName("사용자 수정 실패 - 중복된 사용자명")
    @Transactional
    void updateUser_Fail_DuplicateUsername() throws Exception {
        // 사용자 2명 생성
        String userIdToUpdate = createUserAndGetId("userToUpdate", "update@naver.com");
        String existingUsername = "existingUser";
        createUserAndGetId(existingUsername, "existing@naver.com"); // 이 사용자명은 이미 존재

        UserUpdateRequest updateRequest = createUserUpdateRequest(existingUsername,
            "newemail@naver.com");
        MockMultipartFile updateRequestJson = new MockMultipartFile(
            "userUpdateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(updateRequest)
        );

        ResultActions result = mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/users/" + userIdToUpdate)
                .file(updateRequestJson));

        result.andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(ErrorCode.USER_ALREADY_EXISTS.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.USER_ALREADY_EXISTS.getMessage()))
            .andExpect(jsonPath("$.details.username").value(existingUsername));
    }

    @Test
    @DisplayName("사용자 수정 실패 - 중복된 이메일")
    @Transactional
    void updateUser_Fail_DuplicateEmail() throws Exception {
        String userIdToUpdate = createUserAndGetId("userToUpdateForEmail", "updateEmail@naver.com");

        String existingEmail = "existingEmail@naver.com";
        createUserAndGetId("anotherUserForEmail", existingEmail);

        UserUpdateRequest updateRequest = createUserUpdateRequest("validUpdatedUsername",
            existingEmail);
        MockMultipartFile updateRequestJson = new MockMultipartFile(
            "userUpdateRequest",
            "",
            "application/json",
            objectMapper.writeValueAsBytes(updateRequest)
        );

        ResultActions result = mockMvc.perform(
            multipart(HttpMethod.PATCH, "/api/users/" + userIdToUpdate)
                .file(updateRequestJson));

        result.andExpect(status().isConflict()) // 409 Conflict 예상
            .andExpect(jsonPath("$.code").value(ErrorCode.USER_ALREADY_EXISTS.name()))
            .andExpect(jsonPath("$.message").value(ErrorCode.USER_ALREADY_EXISTS.getMessage()))
            .andExpect(jsonPath("$.details.email").value(existingEmail));
    }

    @Test
    @DisplayName("사용자 삭제 성공 통합 테스트")
    @Transactional
    void deleteUser_Success() throws Exception {
        String userId = createUserAndGetId("deleteme", "deleteme@example.com");

        ResultActions deleteResult = mockMvc.perform(delete("/api/users/" + userId));

        deleteResult.andExpect(status().isNoContent());

        // 삭제된 사용자 조회 시 목록에 없는지 확인
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.id == '" + userId + "')]").doesNotExist());
    }
}
