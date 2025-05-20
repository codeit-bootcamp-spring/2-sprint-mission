package com.sprint.mission.discodeit.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.repository.ChannelJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelUpdateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponseDto;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class ChannelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private ChannelJPARepository channelJPARepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @BeforeEach
    void cleanUp() {
        channelJPARepository.deleteAll(); // 모든 채널 삭제
        userJPARepository.deleteAll();  // 모든 유저 삭제
    }

    @Test
    @DisplayName("[Channel][통합] Channel 기본 생성/조회/수정/삭제 테스트")
    void testChannel() throws Exception {
        // user 생성
        createUser();

        // user 조회
        List<UUID> userIds = userService.findAllUser().stream()
                .map(UserResponseDto::id)
                .toList();

        // channel 객체 생성
        ChannelCreatePublicDto requestPublic = new ChannelCreatePublicDto("테스트방", "테스트입니다.");
        ChannelCreatePrivateDto requestPrivate = new ChannelCreatePrivateDto(userIds);

        // [생성] channel 생성 및 확인
        mockMvc.perform(post("/api/channels/public").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestPublic)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("PUBLIC"))
                .andExpect(jsonPath("$.name").value("테스트방"))
                .andExpect(jsonPath("$.description").value("테스트입니다."));
        mockMvc.perform(post("/api/channels/private").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestPrivate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("PRIVATE"))
                .andExpect(jsonPath("$.participants[0].username").value("메시"))
                .andExpect(jsonPath("$.participants[1].username").value("호날두"));

        // [조회] channel 조회
        mockMvc.perform(get("/api/channels").param("userId", userIds.get(0).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("PUBLIC"))
                .andExpect(jsonPath("$[1].type").value("PRIVATE"))
                .andExpect(jsonPath("$[1].participants[0].username").value("메시"))
                .andExpect(jsonPath("$[1].participants[1].username").value("호날두"));

        // public channel ID 조회
        UUID publicChannelId = channelService.findAllByUserId(userIds.get(0)).stream()
                .filter(channel -> channel.type().toString().equals("PUBLIC"))
                .map(ChannelResponseDto::id)
                .findFirst()
                .orElse(null);
        // public channel ID 조회
        UUID privateChannelId = channelService.findAllByUserId(userIds.get(0)).stream()
                .filter(channel -> channel.type().toString().equals("PRIVATE"))
                .map(ChannelResponseDto::id)
                .findFirst()
                .orElse(null);

        // channel update 객체 생성
        ChannelUpdateDto requestUpdate = new ChannelUpdateDto("방이름변경", "변경되었습니다.");

        // [수정] channel 정보 수정 및 확인
        mockMvc.perform(patch("/api/channels/{channelId}", publicChannelId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("PUBLIC"))
                .andExpect(jsonPath("$.name").value("방이름변경"))
                .andExpect(jsonPath("$.description").value("변경되었습니다."));

        // [삭제] channel 정보 삭제
        mockMvc.perform(delete("/api/channels/{channelId}", privateChannelId))
                .andExpect(status().isNoContent());
    }


    // User 생성
    private void createUser() throws Exception {
        // User 정보 생성
        MockMultipartFile requestPart1 = new MockMultipartFile(
                "userCreateRequest",
                null,
                "application/json",
                objectMapper.writeValueAsBytes(new UserCreateDto("메시", "messi@naver.com", "12345"))
        );

        MockMultipartFile requestPart2 = new MockMultipartFile(
                "userCreateRequest",
                null,
                "application/json",
                objectMapper.writeValueAsBytes(new UserCreateDto("호날두", "ronaldo@naver.com", "54321"))
        );
        // User 정보 API 전달
        mockMvc.perform(multipart("/api/users").file(requestPart1).contentType(MediaType.MULTIPART_FORM_DATA_VALUE));
        mockMvc.perform(multipart("/api/users").file(requestPart2).contentType(MediaType.MULTIPART_FORM_DATA_VALUE));
    }

}
