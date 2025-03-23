package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelRepository;
import com.sprint.mission.discodeit.service.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.basic.repositoryimpl.BasicMessageRepositoryImplement;
import com.sprint.mission.discodeit.basic.serviceimpl.BasicMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Spy
    private MessageRepository messageRepository = new BasicMessageRepositoryImplement();

    @Mock
    private MessageService messageService;

    // 테스트용 데이터
    private UUID userId;
    private UUID channelId;
    private UUID messageId;
    private String content = "테스트 메시지";
    private MessageDto.Response testMessageResponse;

    @BeforeEach
    void setUp() {

        userId = UUID.randomUUID();
        channelId = UUID.randomUUID();
        messageId = UUID.randomUUID();

        User user = new User("test@example.com", "password");
        Channel channel = new Channel("테스트 채널", userId);
        
        lenient().when(userRepository.findByUser(userId)).thenReturn(Optional.of(user));
        lenient().when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
        
        Message message = new Message(userId, channelId, content);
        messageRepository.register(message);
        
        // 테스트용 응답 객체 생성
        testMessageResponse = MessageDto.Response.builder()
            .messageId(messageId)
            .authorId(userId)
            .channelId(channelId)
            .content(content)
            .createdAt(ZonedDateTime.now())
            .build();
    }

    @Test
    void testCreateMessage() throws IOException {
        // 준비
        MessageDto.Create createDto = MessageDto.Create.builder()
            .channelId(channelId)
            .authorId(userId)
            .content("새 테스트 메시지")
            .binaryContents(new ArrayList<>())
            .build();
            
        when(messageService.create(any(MessageDto.Create.class))).thenReturn(testMessageResponse);

        // 실행
        MessageDto.Response response = messageService.create(createDto);

        // 검증
        assertNotNull(response);
        assertEquals(userId, response.getAuthorId());
        assertEquals(channelId, response.getChannelId());
        verify(messageService).create(any(MessageDto.Create.class));
    }

    @Test
    void testFindMessage() {
        // Mock 설정
        when(messageService.findByMessage(messageId)).thenReturn(testMessageResponse);
        
        // 실행
        MessageDto.Response response = messageService.findByMessage(messageId);

        assertNotNull(response);
        assertEquals(messageId, response.getMessageId());
        assertEquals(userId, response.getAuthorId());
        assertEquals(channelId, response.getChannelId());
        verify(messageService).findByMessage(messageId);
    }

    @Test
    void testFindAllMessages() {
        List<MessageDto.Response> messageList = List.of(testMessageResponse);
        when(messageService.findAllMessage()).thenReturn(messageList);
        
        List<MessageDto.Response> responses = messageService.findAllMessage();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(messageService).findAllMessage();
    }

    @Test
    void testFindAllByChannelId() {
        List<MessageDto.Response> messageList = List.of(testMessageResponse);
        when(messageService.findAllByChannelId(channelId)).thenReturn(messageList);
        
        List<MessageDto.Response> responses = messageService.findAllByChannelId(channelId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(channelId, responses.get(0).getChannelId());
        verify(messageService).findAllByChannelId(channelId);
    }

    @Test
    void testUpdateMessage() throws IOException {
        String newContent = "수정된 메시지";
        MessageDto.Update updateDto = MessageDto.Update.builder()
            .messageId(messageId)
            .content(newContent)
            .binaryContents(new ArrayList<>())
            .build();
            
        MessageDto.Response updatedResponse = MessageDto.Response.builder()
            .messageId(messageId)
            .authorId(userId)
            .channelId(channelId)
            .content(newContent)
            .createdAt(ZonedDateTime.now())
            .build();
            
        when(messageService.updateMessage(any(MessageDto.Update.class))).thenReturn(updatedResponse);

        MessageDto.Response response = messageService.updateMessage(updateDto);

        assertNotNull(response);
        assertEquals(messageId, response.getMessageId());
        assertEquals(newContent, response.getContent());
        verify(messageService).updateMessage(any(MessageDto.Update.class));
    }

    @Test
    void testDeleteMessage() {
        when(messageService.deleteMessage(messageId)).thenReturn(true);
        
        boolean deleted = messageService.deleteMessage(messageId);

        assertTrue(deleted);
        verify(messageService).deleteMessage(messageId);
    }
} 