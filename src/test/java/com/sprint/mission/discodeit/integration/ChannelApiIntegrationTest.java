package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.ArrayList;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChannelApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String user1Id;
    private String user2Id;

    private static final String PASSWORD = "123";
    private static final String USER1 = "user1";
    private static final String USER1_EMAIL = "user1@naver.com";
    private static final String USER2 = "user2";
    private static final String USER2_EMAIL = "user2@naver.com";
    private static final String PUBLIC_CHANNEL_NAME = "Public";
    private static final String PUBLIC_CHANNEL_DESC = "Test";
    private static final String PRIVATE_CHANNEL_NAME = "Private";

    @BeforeEach
    @Transactional
    void setUp() throws Exception {
        user1Id = createUser(USER1, USER1_EMAIL);
        user2Id = createUser(USER2, USER2_EMAIL);
    }


    private String createUser(String username, String email) throws Exception {
        UserCreateRequest createRequest = new UserCreateRequest(username, email, PASSWORD);
        MockMultipartFile userCreateRequestPart = new MockMultipartFile(
            "userCreateRequest",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(createRequest)
        );

        MvcResult result = mockMvc.perform(multipart("/api/users")
                .file(userCreateRequestPart)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
            .andExpect(status().isCreated())
            .andReturn();
        UserDto userDto = objectMapper.readValue(result.getResponse().getContentAsString(),
            UserDto.class);
        return userDto.id().toString();
    }


    private String createPublicChannel(String name, String description) throws Exception {
        PublicChannelCreateRequest createRequest = new PublicChannelCreateRequest(
            name, description);

        MvcResult result = mockMvc.perform(post("/api/channels/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn();

        JsonNode responseJson = objectMapper.readTree(result.getResponse().getContentAsString());
        return responseJson.get("id").asText();
    }

    @Test
    @DisplayName("공개 채널 생성 성공")
    @Transactional
    void createPublicChannel_Success() throws Exception {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(
            PUBLIC_CHANNEL_NAME, PUBLIC_CHANNEL_DESC);

        ResultActions result = mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.type").value("PUBLIC"))
            .andExpect(jsonPath("$.name").value(PUBLIC_CHANNEL_NAME))
            .andExpect(jsonPath("$.description").value(PUBLIC_CHANNEL_DESC));
    }

    @Test
    @DisplayName("공개 채널 생성 실패 - 이름 누락")
    @Transactional
    void createPublicChannel_Fail_MissingName() throws Exception {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(
            null, PUBLIC_CHANNEL_DESC);

        // when
        ResultActions result = mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.VALIDATION_FAILED.name()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.details.validationErrors").exists());
    }


    @Test
    @DisplayName("비공개 채널 생성 성공")
    @Transactional
    void createPrivateChannel_Success() throws Exception {
        // given
        List<UUID> participants = Arrays.asList(
            UUID.fromString(user1Id), UUID.fromString(user2Id)
        );

        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
            new ArrayList<>(participants),
            PRIVATE_CHANNEL_NAME
        );

        ResultActions result = mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.type").value("PRIVATE"))
            .andExpect(jsonPath("$.name").value(PRIVATE_CHANNEL_NAME));
    }

    @Test
    @DisplayName("비공개 채널 생성 실패 - 존재하지 않는 사용자 포함")
    @Transactional
    void createPrivateChannel_Fail_InvalidParticipant() throws Exception {
        String nonExistentUserId = UUID.randomUUID().toString();

        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(
            new ArrayList<>(
                Arrays.asList(UUID.fromString(user1Id), UUID.fromString(nonExistentUserId))),
            "Invalid Private Chat"
        );

        ResultActions result = mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.USER_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.details.userId").value(nonExistentUserId));
    }

    @Test
    @DisplayName("채널 수정 성공")
    @Transactional
    void updateChannel_Success() throws Exception {
        String initialChannelName = "Initial Channel";
        String initialChannelDesc = "Initial Desc";
        String updatedChannelName = "Updated Channel Name";
        String updatedChannelDesc = "Updated Description";

        String channelId = createPublicChannel(initialChannelName, initialChannelDesc);

        PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest(
            updatedChannelName, updatedChannelDesc);

        ResultActions result = mockMvc.perform(patch("/api/channels/" + channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(channelId))
            .andExpect(jsonPath("$.name").value(updatedChannelName))
            .andExpect(jsonPath("$.description").value(updatedChannelDesc));
    }

    @Test
    @DisplayName("채널 수정 실패 - 채널 찾을 수 없음")
    @Transactional
    void updateChannel_Fail_NotFound() throws Exception {
        String nonExistentChannelId = UUID.randomUUID().toString();
        PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest(
            "Non Existent Update", "Desc");

        ResultActions result = mockMvc.perform(patch("/api/channels/" + nonExistentChannelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.CHANNEL_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.details.channelId").value(nonExistentChannelId));
    }


    @Test
    @DisplayName("채널 삭제 성공")
    @Transactional
    void deleteChannel_Success() throws Exception {
        String channelName = "Channel to Delete";
        String channelDesc = "Delete Desc";
        String channelId = createPublicChannel(channelName, channelDesc);

        ResultActions result = mockMvc.perform(delete("/api/channels/" + channelId));

        result.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("채널 삭제 실패 - 채널 찾을 수 없음")
    @Transactional
    void deleteChannel_Fail_NotFound() throws Exception {
        String nonExistentChannelId = UUID.randomUUID().toString();

        ResultActions result = mockMvc.perform(delete("/api/channels/" + nonExistentChannelId));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.CHANNEL_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.details.channelId").value(nonExistentChannelId));
    }

    @Test
    @DisplayName("사용자 ID로 모든 채널 조회 성공")
    @Transactional
    void findAllChannelsByUserId_Success() throws Exception {
        String publicChannelName = "User1 Public";
        createPublicChannel(publicChannelName, "Desc");

        PrivateChannelCreateRequest privateRequest = new PrivateChannelCreateRequest(
            new ArrayList<>(Arrays.asList(UUID.fromString(user1Id), UUID.fromString(user2Id))),
            "User1-User2 Private"
        );

        mockMvc.perform(post("/api/channels/private")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(privateRequest)))
            .andExpect(status().isCreated());

        ResultActions result = mockMvc.perform(get("/api/channels")
            .param("userId", user1Id));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
            .andExpect(jsonPath("$[?(@.name == 'User1 Public')]").exists());
    }

    @Test
    @DisplayName("사용자 ID로 채널 조회 실패 - 사용자 찾을 수 없음")
    @Transactional
    void findAllChannelsByUserId_Fail_UserNotFound() throws Exception {
        String nonExistentUserId = UUID.randomUUID().toString();

        ResultActions result = mockMvc.perform(get("/api/channels")
            .param("userId", nonExistentUserId));

        result.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.USER_NOT_FOUND.name()))
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.details.userId").value(nonExistentUserId));
    }
} 