package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private PageResponseMapper pageMapper;

    @Mock
    private BinaryContentService binaryContentService;

    @InjectMocks
    private BasicMessageService messageService;

    private static final String TEST_AUTHOR_NAME = "보내는 사람";
    private static final String TEST_AUTHOR_EMAIL = "author@naver.com";
    private static final String TEST_AUTHOR_PASSWORD = "123";
    private static final String TEST_CHANNEL_NAME = "Test PublicChannel";
    private static final String TEST_CHANNEL_DESC = "Desc";
    private static final String TEST_MESSAGE_CONTENT = "테스트용";
    private static final String TEST_MESSAGE_DTO_CONTENT = "테스트용 메세지 보내기";
    private static final String CREATE_MESSAGE_CONTENT = "Hello";
    private static final String CREATE_MESSAGE_WITH_ATTACHMENTS = "With attachments";
    private static final String UPDATE_MESSAGE_CONTENT = "업데이트 메세지";

    private static final String FILE1_NAME = "file1.txt";
    private static final String FILE2_NAME = "file2.jpg";
    private static final String TEXT_MIME_TYPE = "text/plain";
    private static final String IMAGE_MIME_TYPE = "image/jpeg";
    private static final long FILE1_SIZE = 100L;
    private static final long FILE2_SIZE = 200L;

    private UUID messageId;
    private UUID channelId;
    private UUID authorId;
    private User author;
    private Channel channel;
    private Message message;
    private MessageDto messageDto;
    private UserDto authorDto;
    private MultipartFile mockFile1;
    private MultipartFile mockFile2;
    private Instant now;

    @BeforeEach
    void setUp() {
        messageId = UUID.randomUUID();
        channelId = UUID.randomUUID();
        authorId = UUID.randomUUID();
        now = Instant.now();

        author = new User(TEST_AUTHOR_NAME, TEST_AUTHOR_EMAIL, TEST_AUTHOR_PASSWORD, null);
        channel = new Channel(ChannelType.PUBLIC, TEST_CHANNEL_NAME, TEST_CHANNEL_DESC);
        message = new Message(TEST_MESSAGE_CONTENT, channel, author, new ArrayList<>());

        authorDto = new UserDto(authorId, "author", TEST_AUTHOR_EMAIL, null, true);
        messageDto = new MessageDto(messageId, now, now, TEST_MESSAGE_DTO_CONTENT, channelId,
            authorDto,
            Collections.emptyList());

        mockFile1 = mock(MultipartFile.class);
        mockFile2 = mock(MultipartFile.class);
    }

    @Test
    @DisplayName("메시지 생성 성공 (첨부파일 X)")
    void createMessage_Success_NoAttachments() {
        MessageCreateRequest request = new MessageCreateRequest(CREATE_MESSAGE_CONTENT, channelId,
            authorId);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(authorId)).willReturn(Optional.of(author));
        given(messageRepository.save(any(Message.class))).willAnswer(
            invocation -> invocation.getArgument(0));
        given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

        MessageDto result = messageService.create(request, Collections.emptyList());

        assertThat(result).isEqualTo(messageDto);
        verify(channelRepository).findById(channelId);
        verify(userRepository).findById(authorId);
        verify(messageRepository).save(any(Message.class));
        verify(messageMapper).toDto(any(Message.class));
        verify(binaryContentService, never()).create(any(MultipartFile.class));
    }

    @Test
    @DisplayName("메시지 생성 성공 (첨부파일 O)")
    void createMessage_Success_WithAttachments() {
        MessageCreateRequest request = new MessageCreateRequest(CREATE_MESSAGE_WITH_ATTACHMENTS,
            channelId, authorId);
        List<MultipartFile> files = List.of(mockFile1, mockFile2);
        BinaryContentDto mockBinaryDto1 = new BinaryContentDto(UUID.randomUUID(), FILE1_NAME,
            FILE1_SIZE, TEXT_MIME_TYPE);
        BinaryContentDto mockBinaryDto2 = new BinaryContentDto(UUID.randomUUID(), FILE2_NAME,
            FILE2_SIZE, IMAGE_MIME_TYPE);
        BinaryContent mockBinaryEntity1 = new BinaryContent(TEXT_MIME_TYPE, FILE1_NAME, FILE1_SIZE);
        BinaryContent mockBinaryEntity2 = new BinaryContent(IMAGE_MIME_TYPE, FILE2_NAME,
            FILE2_SIZE);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(authorId)).willReturn(Optional.of(author));
        given(mockFile1.isEmpty()).willReturn(false);
        given(mockFile1.getOriginalFilename()).willReturn(FILE1_NAME);
        given(mockFile2.isEmpty()).willReturn(false);
        given(mockFile2.getOriginalFilename()).willReturn(FILE2_NAME);

        given(binaryContentService.create(mockFile1)).willReturn(mockBinaryDto1);
        given(binaryContentService.create(mockFile2)).willReturn(mockBinaryDto2);
        given(binaryContentRepository.findById(mockBinaryDto1.id())).willReturn(
            Optional.of(mockBinaryEntity1));
        given(binaryContentRepository.findById(mockBinaryDto2.id())).willReturn(
            Optional.of(mockBinaryEntity2));

        given(messageRepository.save(any(Message.class))).willAnswer(
            invocation -> invocation.getArgument(0));
        MessageDto dtoWithAttachments = new MessageDto(messageId, now, now, request.content(),
            channelId, authorDto, List.of(mockBinaryDto1, mockBinaryDto2));
        given(messageMapper.toDto(any(Message.class))).willReturn(dtoWithAttachments);

        MessageDto result = messageService.create(request, files);

        assertThat(result).isNotNull();
        assertThat(result.attachments()).hasSize(2);
        verify(binaryContentService, times(2)).create(any(MultipartFile.class));
        verify(binaryContentRepository, times(2)).findById(any(UUID.class));
        verify(messageRepository).save(argThat(m -> m.getAttachments().size() == 2));
    }

    @Test
    @DisplayName("메시지 생성 실패 (채널 없음)")
    void createMessage_Failure_ChannelNotFound() {
        MessageCreateRequest request = new MessageCreateRequest(CREATE_MESSAGE_CONTENT, channelId,
            authorId);
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        assertThatThrownBy(
            () -> messageService.create(request, Collections.emptyList())).isInstanceOf(
            ChannelException.class);
    }

    @Test
    @DisplayName("메시지 생성 실패 (작성자 없음)")
    void createMessage_Failure_AuthorNotFound() {
        MessageCreateRequest request = new MessageCreateRequest(CREATE_MESSAGE_CONTENT, channelId,
            authorId);
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(authorId)).willReturn(Optional.empty());

        assertThatThrownBy(
            () -> messageService.create(request, Collections.emptyList())).isInstanceOf(
            UserNotFoundException.class);
    }

    @Test
    @DisplayName("메시지 업데이트 성공")
    void updateMessage_Success() {
        MessageUpdateRequest request = new MessageUpdateRequest(UPDATE_MESSAGE_CONTENT);
        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
        given(messageRepository.save(any(Message.class))).willAnswer(
            invocation -> invocation.getArgument(0));
        MessageDto updatedDto = new MessageDto(messageId, now, Instant.now(), request.newContent(),
            channelId, authorDto, Collections.emptyList());
        given(messageMapper.toDto(any(Message.class))).willReturn(updatedDto);

        MessageDto result = messageService.update(messageId, request);

        assertThat(result.content()).isEqualTo(request.newContent());
        verify(messageRepository).findById(messageId);
        verify(messageRepository).save(message);
        verify(messageMapper).toDto(message);
    }

    @Test
    @DisplayName("메시지 업데이트 실패 (메시지 없음)")
    void updateMessage_Failure_NotFound() {
        MessageUpdateRequest request = new MessageUpdateRequest(TEST_MESSAGE_DTO_CONTENT);
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.update(messageId, request)).isInstanceOf(
            MessageException.class);
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void deleteMessage_Success() {
        given(messageRepository.existsById(messageId)).willReturn(true);
        doNothing().when(messageRepository).deleteById(messageId);

        messageService.delete(messageId);

        verify(messageRepository).existsById(messageId);
        verify(messageRepository).deleteById(messageId);
    }

    @Test
    @DisplayName("메시지 삭제 실패 (메시지 없음)")
    void deleteMessage_Failure_NotFoundOnDelete() {
        given(messageRepository.existsById(messageId)).willReturn(false);

        assertThatThrownBy(() -> messageService.delete(messageId)).isInstanceOf(
            MessageException.class);
    }

    @Test
    @DisplayName("채널 ID로 메시지 목록 조회 성공 (커서 없음)")
    void findAllByChannelId_Success_NoCursor() {
        int size = 10;
        List<Message> messages = Collections.singletonList(message);
        List<MessageDto> messageDtos = Collections.singletonList(messageDto);
        PageResponse<MessageDto> expectedPageResponse = new PageResponse<>(
            messageDtos, null, size, false, (long) messages.size());

        given(channelRepository.existsById(channelId)).willReturn(true);
        given(messageRepository.findByChannelIdOrderByCreatedAtDesc(eq(channelId),
            any(PageRequest.class))).willReturn(messages);

        given(pageMapper.messageListToPageResponse(anyList(), eq(size), eq(false)))
            .willAnswer(invocation -> {
                return expectedPageResponse;
            });

        PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, size);

        assertThat(result).isEqualTo(expectedPageResponse);
        verify(channelRepository).existsById(channelId);
        verify(messageRepository).findByChannelIdOrderByCreatedAtDesc(eq(channelId),
            any(PageRequest.class));
    }

    @Test
    @DisplayName("채널 ID로 메시지 목록 조회 성공 (커서 사용)")
    void findAllByChannelId_Success_WithCursor() {
        int size = 10;
        String cursor = "2022-01-01T00:00:00Z";
        Instant cursorInstant = Instant.parse(cursor);
        List<Message> messages = Collections.singletonList(message);
        List<MessageDto> messageDtos = Collections.singletonList(messageDto);
        PageResponse<MessageDto> expectedPageResponse = new PageResponse<>(
            messageDtos, null, size, false, (long) messages.size());

        given(channelRepository.existsById(channelId)).willReturn(true);
        given(messageRepository.findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(
            eq(channelId), eq(cursorInstant), any(PageRequest.class))).willReturn(messages);

        given(pageMapper.messageListToPageResponse(anyList(), eq(size), eq(false)))
            .willReturn(expectedPageResponse);

        PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, cursor,
            size);

        assertThat(result).isEqualTo(expectedPageResponse);
        verify(channelRepository).existsById(channelId);
        verify(messageRepository).findByChannelIdAndCreatedAtLessThanOrderByCreatedAtDesc(
            eq(channelId), eq(cursorInstant), any(PageRequest.class));
    }

    @Test
    @DisplayName("채널 ID로 메시지 목록 조회 실패 (채널 없음)")
    void findAllByChannelId_Failure_ChannelNotFound() {
        given(channelRepository.existsById(channelId)).willReturn(false);
        //assertThatThrownBy 예외 발생 상황 테스트
        assertThatThrownBy(() -> messageService.findAllByChannelId(channelId, null, 10))
            // 같은 클래스 예외인지 확인
            .isInstanceOf(ChannelException.class);
    }
}
