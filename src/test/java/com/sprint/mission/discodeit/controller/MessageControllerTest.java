package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("[MessageController][createMessage] Message Controller 테스트")
    public void createMessage() throws Exception {
        User user = createUserWithId("메시", "messi@naver.com", "12345");
        Channel channel = createChannelWithId(ChannelType.PUBLIC, "공개방", "공지사항 전달");
        MockMultipartFile requestPart = new MockMultipartFile(
                "messageCreateRequest",
                null,
                "application/json",
                objectMapper.writeValueAsBytes(new MessageCreateDto("첫인사드립니다.", channel.getId(), user.getId()))
        );

        UserResponseDto responseUser = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                true
        );

        Message message = createMessageWithId("첫인사드립니다.", channel, user);
        MessageResponseDto response = new MessageResponseDto(
                message.getId(),
                Instant.now(),
                Instant.now(),
                message.getContent(),
                channel.getId(),
                responseUser,
                null
        );

        given(messageService.create(any(MessageCreateDto.class), any())).willReturn(response);

        mockMvc.perform(multipart("/api/messages").file(requestPart).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("첫인사드립니다."))
                .andExpect(jsonPath("$.author.username").value("메시"));
    }


    @Test
    @DisplayName("[ChannelController][updateMessage] Message Controller 테스트")
    public void updateMessage() throws Exception {
        User user = createUserWithId("메시", "messi@naver.com", "12345");
        Channel channel = createChannelWithId(ChannelType.PUBLIC, "공개방", "공지사항 전달");

        UserResponseDto responseUser = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                true
        );

        MessageUpdateDto updateMessage = new MessageUpdateDto("변경됐습니다.");
        Message message = createMessageWithId("첫인사드립니다.", channel, user);
        MessageResponseDto response = new MessageResponseDto(
                message.getId(),
                Instant.now(),
                Instant.now(),
                updateMessage.newContent(),
                channel.getId(),
                responseUser,
                null
        );

        given(messageService.update(any(UUID.class), any(MessageUpdateDto.class))).willReturn(response);

        mockMvc.perform(patch("/api/messages/{messageId}", message.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(updateMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("변경됐습니다."));
    }


    @Test
    @DisplayName("[ChannelController][findMessagesByChannelId] Message Controller 테스트")
    public void findMessagesByChannelId() throws Exception {
        User user = createUserWithId("메시", "messi@naver.com", "12345");
        Channel channel = createChannelWithId(ChannelType.PUBLIC, "공개방", "공지사항 전달");

        UserResponseDto responseUser = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                true
        );

        List<Message> messages = List.of(
                createMessageWithId("첫인사드립니다.", channel, user),
                createMessageWithId("두번째 인사입니다.", channel, user)
        );
        List<MessageResponseDto> responseMessages = messages.stream()
                .map(message -> new MessageResponseDto(
                        message.getId(),
                        Instant.now(),
                        Instant.now(),
                        message.getContent(),
                        channel.getId(),
                        responseUser,
                        null))
                .toList();

        Pageable pageable = PageRequest.of(0, 2);
        Page<MessageResponseDto> responsePageMessage = new PageImpl<>(responseMessages, pageable, messages.size());
        PageResponseDto<MessageResponseDto> responsePage = new PageResponseDto<>(
                responsePageMessage.getContent(),
                null,
                responsePageMessage.getSize(),
                responsePageMessage.hasNext(),
                responsePageMessage.getTotalElements()
        );

        given(messageService.findAllByChannelId(any(), any(), any())).willReturn(responsePage);

        mockMvc.perform(get("/api/messages")
                .param("channelId", channel.getId().toString())
                .param("pageable", pageable.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("첫인사드립니다."))
                .andExpect(jsonPath("$.content[1].content").value("두번째 인사입니다."))
                .andExpect(jsonPath("$.totalElements").value("2"));

    }


    // User 생성
    private User createUserWithId(String name, String email, String password) {
        User user = new User(name, email, password, null);
        ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
        return user;
    }

    // Channel 생성
    private Channel createChannelWithId(ChannelType channelType, String name, String description) {
        Channel channel = new Channel(channelType, name, description);
        ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());
        return channel;
    }

    // Message 생성
    private Message createMessageWithId(String content, Channel channel, User user) {
        Message message = new Message(content, channel, user, null);
        ReflectionTestUtils.setField(message, "id", UUID.randomUUID());
        return message;
    }
}
