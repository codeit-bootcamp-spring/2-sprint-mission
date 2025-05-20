package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.lang.reflect.Field;
import java.util.*;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
public class BasicMessageServiceTest {

    @Mock private MessageRepository messageRepository;
    @Mock private ChannelRepository channelRepository;
    @Mock private UserRepository userRepository;
    @Mock private BinaryContentRepository binaryContentRepository;
    @Mock private MessageMapper messageMapper;
    @Mock private BinaryContentStorage binaryContentStorage;
    @Mock private PageResponseMapper pageResponseMapper;

    @InjectMocks
    private BasicMessageService basicMessageService;

    private Channel channel;
    private User author;
    private Message message;
    private MessageDto messageDto;
    private BinaryContent binaryContent;
    private BinaryContentDto binaryContentDto;

    @BeforeEach
    void setUp() {
        // Channel, User, BinaryContent, Message 엔티티 초기화
        channel = Channel.builder().name("test-channel").type(ChannelType.PUBLIC).build();
        setId(channel, UUID.randomUUID());

        author = User.builder().username("author").email("author@test.com").build();
        setId(author, UUID.randomUUID());

        binaryContent = BinaryContent.builder()
                .fileName("file.jpg")
                .size(1024L)
                .contentType("image/jpeg")
                .build();
        setId(binaryContent, UUID.randomUUID());

        binaryContentDto = new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType()
        );

        message = Message.builder()
                .content("Hello World")
                .channel(channel)
                .author(author)
                .build();
        setId(message, UUID.randomUUID());
        message.addAttachment(binaryContent);

        messageDto = new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                channel.getId(),
                new UserDto(
                        author.getId(),
                        author.getUsername(),
                        author.getEmail(),
                        null,
                        true
                ),
                List.of(binaryContentDto)
        );
    }

    @Test
    @DisplayName("메시지 생성 성공 - 첨부파일 포함")
    void createMessage_Success_WithAttachments() {
        // Given
        MessageCreateRequest request = new MessageCreateRequest(
                "Hello World",
                channel.getId(),
                author.getId()
        );
        BinaryContentCreateRequest attachmentRequest = new BinaryContentCreateRequest(
                "file.jpg", "image/jpeg", new byte[1024]
        );
        List<BinaryContentCreateRequest> attachments = List.of(attachmentRequest);

        given(channelRepository.findById(channel.getId())).willReturn(Optional.of(channel));
        given(userRepository.findById(author.getId())).willReturn(Optional.of(author));
        given(messageRepository.save(any(Message.class))).willReturn(message);
        given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(binaryContent);
        willDoNothing().given(binaryContentStorage).put(any(UUID.class), any(byte[].class));
        given(messageMapper.toDto(message)).willReturn(messageDto);

        // When
        MessageDto result = basicMessageService.create(request, attachments);

        // Then
        then(channelRepository).should().findById(channel.getId());
        then(userRepository).should().findById(author.getId());
        then(messageRepository).should(times(2)).save(any(Message.class)); // 최초, 첨부 후
        then(binaryContentRepository).should().save(any(BinaryContent.class));
        then(binaryContentStorage).should().put(binaryContent.getId(), attachmentRequest.bytes());
        assertThat(result).isEqualTo(messageDto);
    }

    @Test
    @DisplayName("메시지 생성 실패 - 채널 없음")
    void createMessage_Fail_ChannelNotFound() {
        // Given
        MessageCreateRequest request = new MessageCreateRequest(
                "Hello",
                UUID.randomUUID(),
                author.getId()
        );
        List<BinaryContentCreateRequest> attachments = Collections.emptyList();

        given(channelRepository.findById(request.channelId())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> basicMessageService.create(request, attachments))
                .isInstanceOf(ChannelNotFoundException.class);

        then(messageRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("메시지 조회 성공")
    void findMessage_Success() {
        // Given
        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));
        given(messageMapper.toDto(message)).willReturn(messageDto);

        // When
        MessageDto result = basicMessageService.find(message.getId());

        // Then
        assertThat(result).isEqualTo(messageDto);
    }

    @Test
    @DisplayName("메시지 조회 실패 - 메시지 없음")
    void findMessage_Fail_NotFound() {
        UUID invalidId = UUID.randomUUID();
        given(messageRepository.findById(invalidId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> basicMessageService.find(invalidId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("채널별 메시지 전체 조회 성공")
    void findAllByChannelId_Success() {
        // Given
        List<Message> messages = List.of(message);
        List<MessageDto> messageDtos = List.of(messageDto);

        given(messageRepository.findAllByChannelId(channel.getId())).willReturn(messages);
        given(messageMapper.toDtoList(messages)).willReturn(messageDtos);

        // When
        List<MessageDto> result = basicMessageService.findAllByChannelId(channel.getId());

        // Then
        assertThat(result).isEqualTo(messageDtos);
    }

    @Test
    @DisplayName("메시지 업데이트 성공")
    void updateMessage_Success() {
        // Given
        MessageUpdateRequest updateRequest = new MessageUpdateRequest("Updated content");
        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));
        given(messageRepository.save(message)).willReturn(message);
        given(messageMapper.toDto(message)).willReturn(messageDto);

        // When
        MessageDto result = basicMessageService.update(message.getId(), updateRequest);

        // Then
        assertThat(message.getContent()).isEqualTo("Updated content");
        assertThat(result).isEqualTo(messageDto);
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void deleteMessage_Success() {
        // Given
        given(messageRepository.findById(message.getId())).willReturn(Optional.of(message));
        willDoNothing().given(binaryContentRepository).deleteById(binaryContent.getId());
        willDoNothing().given(messageRepository).deleteById(message.getId());

        // When
        basicMessageService.delete(message.getId());

        // Then
        then(binaryContentRepository).should().deleteById(binaryContent.getId());
        then(messageRepository).should().deleteById(message.getId());
    }

    // 엔티티의 id를 reflection으로 세팅하는 유틸리티
    private void setId(Object entity, UUID id) {
        try {
            Class<?> clazz = entity.getClass();
            while (clazz != null) { // 모든 상위 클래스 탐색
                try {
                    Field idField = clazz.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(entity, id);
                    return;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass(); // 상위 클래스로 이동
                }
            }
            throw new NoSuchFieldException("id 필드를 찾을 수 없음");
        } catch (Exception e) {
            throw new RuntimeException("ID 설정 실패", e);
        }
    }
}
