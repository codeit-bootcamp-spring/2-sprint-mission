package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.ErrorCode;
import com.sprint.mission.discodeit.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exceptions.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.PageMapper;
import com.sprint.mission.discodeit.mapper.ResponseMapStruct;
import com.sprint.mission.discodeit.repository.ChannelJPARepository;
import com.sprint.mission.discodeit.repository.MessageJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageCreateDto;
import com.sprint.mission.discodeit.service.dto.request.messagedto.MessageUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
public class MessageServiceUnitTest {

    @Mock
    private MessageJPARepository messageJpaRepository;

    @Mock
    private UserJPARepository userJpaRepository;

    @Mock
    private ChannelJPARepository channelJpaRepository;

    @Mock
    private PageMapper pageMapper;

    @Mock
    private ResponseMapStruct responseMapStruct;

    @InjectMocks
    private BasicMessageService basicMessageService;

    private User user;
    private Channel channel;
    private Message message;


    void setUp() {
        UUID userId = UUID.randomUUID();
        user = new User("메시", "messi@naver.com", "12345", null);
        ReflectionTestUtils.setField(user, "id", userId);

        UUID channelId = UUID.randomUUID();
        channel = new Channel(ChannelType.PUBLIC, "공개방", "공개방입니다.");
        ReflectionTestUtils.setField(channel, "id", channelId);

        UUID messageId = UUID.randomUUID();
        message = new Message("반갑습니다.", channel, user, null);
        ReflectionTestUtils.setField(message, "id", messageId);
    }

    @Test
    @DisplayName("[Message][create] 메시지 생성 테스트")
    public void testCreateMessage() {
        setUp();
        MessageCreateDto messageCreate = new MessageCreateDto("반갑습니다.", channel.getId(), user.getId());
        UserResponseDto userResponse = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                true
        );

        MessageResponseDto responseMessage = new MessageResponseDto(
                message.getId(),
                Instant.now(),
                Instant.now(),
                message.getContent(),
                channel.getId(),
                userResponse,
                List.of()
        );

        given(userJpaRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(channelJpaRepository.findById(channel.getId())).willReturn(Optional.of(channel));
        given(messageJpaRepository.save(any(Message.class))).willReturn(message);
        given(responseMapStruct.toMessageDto(any(Message.class))).willReturn(responseMessage);

        MessageResponseDto result = basicMessageService.create(messageCreate, List.of());

        then(userJpaRepository).should().findById(user.getId());
        then(channelJpaRepository).should().findById(channel.getId());
        then(messageJpaRepository).should().save(any(Message.class));
        then(responseMapStruct).should().toMessageDto(any(Message.class));

        assertEquals("반갑습니다.", result.content());
        assertEquals("메시", result.author().username());
        assertEquals("messi@naver.com", result.author().email());
    }

    @Test
    @DisplayName("[Message][create] 존재하지 않는 사용자 메시지 생성 예외 테스트")
    public void testCreateMessageWithUserNotFoundException() {
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        MessageCreateDto messageCreateDto = new MessageCreateDto("반갑습니다.", channelId, userId);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                ()-> basicMessageService.create(messageCreateDto, List.of()));

        then(userJpaRepository).should().findById(userId);
        then(channelJpaRepository).should(never()).findById(channelId);
        then(messageJpaRepository).should(never()).save(any(Message.class));
        then(responseMapStruct).should(never()).toMessageDto(any(Message.class));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("[Message][create] 존재하지 않는 채널 메시지 생성 예외 테스트")
    public void testCreateMessageWithChannelNotFoundException() {
            UUID userId = UUID.randomUUID();
            User user = new User("메시", "messi@naver.com", "12345", null);
            ReflectionTestUtils.setField(user, "id", userId);

            UUID channelId = UUID.randomUUID();

            MessageCreateDto messageCreateDto = new MessageCreateDto("반갑습니다.", channelId, userId);

            given(userJpaRepository.findById(user.getId())).willReturn(Optional.of(user));

            ChannelNotFoundException exception = assertThrows(ChannelNotFoundException.class,
                    ()-> basicMessageService.create(messageCreateDto, List.of()));

            then(userJpaRepository).should().findById(userId);
            then(channelJpaRepository).should().findById(channelId);
            then(messageJpaRepository).should(never()).save(any(Message.class));
            then(responseMapStruct).should(never()).toMessageDto(any(Message.class));

            assertEquals(ErrorCode.CHANNEL_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    @DisplayName("[Message][update] 메시지 수정 테스트")
    public void testUpdateMessage() {
        setUp();
        MessageUpdateDto messageUpdate = new MessageUpdateDto("변경합니다.");
        UserResponseDto userResponse = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                true
        );

        MessageResponseDto responseMessage = new MessageResponseDto(
                message.getId(),
                Instant.now(),
                Instant.now(),
                messageUpdate.newContent(),
                channel.getId(),
                userResponse,
                List.of()
        );

        given(messageJpaRepository.findById(message.getId())).willReturn(Optional.of(message));
        given(messageJpaRepository.save(any(Message.class))).willReturn(message);
        given(responseMapStruct.toMessageDto(any(Message.class))).willReturn(responseMessage);

        MessageResponseDto result = basicMessageService.update(message.getId(), messageUpdate);

        assertEquals("변경합니다.", result.content());
    }

    @Test
    @DisplayName("[Message][update] 존재하지 않는 메시지 수정 예외 테스트")
    public void testUpdateMessageWithMessageNotFoundException() {
        UUID messageId = UUID.randomUUID();
        MessageUpdateDto messageUpdate = new MessageUpdateDto("변경합니다.");

        given(messageJpaRepository.findById(messageId)).willReturn(Optional.empty());

        MessageNotFoundException exception = assertThrows(MessageNotFoundException.class,
                () -> basicMessageService.update(messageId, messageUpdate));

        then(messageJpaRepository).should().findById(messageId);
        then(messageJpaRepository).should(never()).save(any(Message.class));
        then(responseMapStruct).should(never()).toMessageDto(any(Message.class));

        assertEquals(ErrorCode.MESSAGE_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    @DisplayName("[Message][delete] 메시지 삭제 테스트")
    public void testDeleteMessage() {
        setUp();
        given(messageJpaRepository.findById(message.getId())).willReturn(Optional.of(message));
        basicMessageService.delete(message.getId());
        then(messageJpaRepository).should().delete(any(Message.class));
    }

    @Test
    @DisplayName("[Message][delete] 존재하지 않는 메시지 삭제 예외 테스트")
    public void testDeleteMessageWithMessageNotFoundException() {
        UUID messageId = UUID.randomUUID();

        given(messageJpaRepository.findById(messageId)).willReturn(Optional.empty());

        MessageNotFoundException exception = assertThrows(MessageNotFoundException.class,
                () -> basicMessageService.delete(messageId));

        then(messageJpaRepository).should(never()).delete(any(Message.class));
    }


    @Test
    @DisplayName("[Message][findAll] 채널 ID로 메시지 조회 테스트")
    public void testFindAllByChannelMessage() {
        setUp();
        Pageable pageable = PageRequest.of(0, 10);

        UserResponseDto userResponse = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                true
        );

        MessageResponseDto responseMessage = new MessageResponseDto(
                message.getId(),
                Instant.now(),
                Instant.now(),
                message.getContent(),
                channel.getId(),
                userResponse,
                List.of()
        );

        Page<Message> pageMessage = new PageImpl<>(List.of(message), pageable, 1);
        Page<MessageResponseDto> pageMessageResponse = new PageImpl<>(List.of(responseMessage), pageable, 1);

        PageResponseDto<MessageResponseDto> pageResponse = new PageResponseDto<>(
                pageMessageResponse.getContent(),
                null,
                pageMessageResponse.getSize(),
                pageMessageResponse.hasNext(),
                pageMessageResponse.getTotalElements()
        );

        given(messageJpaRepository.findByChannel_IdEntityGraph(channel.getId(), pageable)).willReturn(pageMessage);
        given(responseMapStruct.toMessageDto(any(Message.class))).willReturn(responseMessage);
        given(pageMapper.fromPage(pageMessageResponse)).willReturn(pageResponse);
        given(pageMapper.fromPage(pageMessageResponse)).willAnswer(invocation -> {
            Page<MessageResponseDto> message = invocation.getArgument(0);
            return new PageResponseDto<MessageResponseDto>(
                    message.getContent(),
                    null,
                    message.getSize(),
                    message.hasNext(),
                    message.getTotalElements()
            );
        });

        PageResponseDto<MessageResponseDto> result = basicMessageService.findAllByChannelId(channel.getId(), null, pageable);

        assertEquals(pageResponse, result);
    }

}
