package com.sprint.mission.discodeit.Integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.repository.ChannelJPARepository;
import com.sprint.mission.discodeit.repository.MessageJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePrivateDto;
import com.sprint.mission.discodeit.service.dto.request.channeldto.ChannelCreatePublicDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.service.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.config.location=classpath:/application.yaml"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MessageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJPARepository userJPARepository;

    @Autowired
    private ChannelJPARepository channelJPARepository;

    @Autowired
    private MessageJPARepository messageJPARepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MessageService messageService;


    @BeforeEach
    void cleanUp() {
        channelJPARepository.deleteAll(); // 모든 채널 삭제
        userJPARepository.deleteAll(); // 모든 유저 삭제
        messageJPARepository.deleteAll(); // 모든 메시지 삭제
    }

    @Test
    @DisplayName("[Message][통합] Message 기본 생성/조회/수정/삭제 테스트")
    void testMessage() throws Exception {
        // user 및 channel 생성
        createUserAndChannel();

        // user ID 및 channel ID 조회
        UUID userId = userService.findAllUser().stream()
                .filter(u->u.username().equals("메시"))
                .map(UserResponseDto::id)
                .findFirst()
                .orElse(null);
        UUID channelId = channelService.findAllByUserId(userId).stream()
                .filter(c->c.type().toString().equals("PUBLIC"))
                .map(ChannelResponseDto::id)
                .findFirst()
                .orElse(null);

        // message 정보 생성
        MockMultipartFile requestPart = new MockMultipartFile(
                "messageCreateRequest",
                null,
                "application/json",
                objectMapper.writeValueAsBytes(new MessageCreateDto("첫인사드립니다.", channelId, userId))
        );

        // [생성] message 생성
        mockMvc.perform(multipart("/api/messages").file(requestPart).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("첫인사드립니다."))
                .andExpect(jsonPath("$.author.username").value("메시"));

        // 페이지네이션을 위해 객체 생성
        Pageable pageable = PageRequest.of(0, 2);

        // [조회] message 조회
        mockMvc.perform(get("/api/messages")
                        .param("channelId", String.valueOf(channelId))
                        .param("pageable", String.valueOf(pageable)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("첫인사드립니다."))
                .andExpect(jsonPath("$.totalElements").value("1"));

        // message ID 조회
        PageResponseDto<MessageResponseDto> messages = messageService.findAllByChannelId(channelId, null, pageable);
        UUID messageId = messages.content().stream()
                .filter(u -> u.content().equals("첫인사드립니다."))
                .map(MessageResponseDto::id)
                .findFirst()
                .orElse(null);

        // update message 객체 생성
        MessageUpdateDto updateMessage = new MessageUpdateDto("변경됐습니다.");

        // [수정] message 수정 및 확인
        mockMvc.perform(patch("/api/messages/{messageId}", messageId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(updateMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("변경됐습니다."));

        // [삭제] message 삭제
        mockMvc.perform(delete("/api/messages/{messageId}", messageId))
                .andExpect(status().isNoContent());

    }



    private void createUserAndChannel() throws Exception {
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

        // user 조회
        List<UUID> userIds = userService.findAllUser().stream()
                .map(UserResponseDto::id)
                .toList();

        // channel 객체 생성
        ChannelCreatePublicDto requestPublic = new ChannelCreatePublicDto("테스트방", "테스트입니다.");
        ChannelCreatePrivateDto requestPrivate = new ChannelCreatePrivateDto(userIds);

        // [생성] channel 생성 및 확인
        mockMvc.perform(post("/api/channels/public").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestPublic)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/channels/private").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(requestPrivate)))
                .andExpect(status().isCreated());
    }

}
