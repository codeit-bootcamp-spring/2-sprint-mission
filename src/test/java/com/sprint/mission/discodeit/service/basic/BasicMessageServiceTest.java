package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@ExtendWith(MockitoExtension.class)
@DisplayName("BasicMessageService Test")
public class BasicMessageServiceTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private PageResponseMapper pageResponseMapper;

    @InjectMocks
    private BasicMessageService messageService;

    @Test
    @DisplayName("메시지 생성 성공")
    void createMessage_success() {
        // given
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        String content = "Hello";

        MessageCreateDto createDto = new MessageCreateDto(userId, channelId, content);
        BinaryContentCreateDto fileDto = new BinaryContentCreateDto("file.png", "image/png", new byte[]{1, 2});
        User user = mock(User.class);
        Channel channel = mock(Channel.class);
        MessageDto expectedDto = mock(MessageDto.class);
        Message savedMessage = new Message(user, channel, content, List.of());

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(binaryContentRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));
        given(messageRepository.save(any())).willReturn(savedMessage);
        given(messageMapper.toDto(any(Message.class))).willReturn(expectedDto);

        // when
        MessageDto result = messageService.create(createDto, List.of(fileDto));

        // then
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("메시지 생성 실패 - 존재하지 않는 사용자")
    void createMessage_fail_userNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        MessageCreateDto createDto = new MessageCreateDto(userId, UUID.randomUUID(), "Hi");

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.create(createDto, List.of()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("메시지 수정 성공")
    void updateMessage_success() {
        // given
        UUID messageId = UUID.randomUUID();
        MessageUpdateDto updateDto = new MessageUpdateDto("updated");

        Message message = mock(Message.class);
        MessageDto expectedDto = mock(MessageDto.class);

        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
        given(messageMapper.toDto(message)).willReturn(expectedDto);

        // when
        MessageDto result = messageService.update(messageId, updateDto);

        // then
        assertThat(result).isEqualTo(expectedDto);
        then(message).should().update(updateDto.newContent());
    }

    @Test
    @DisplayName("메시지 수정 실패 - 메시지 없음")
    void updateMessage_fail_notFound() {
        // given
        UUID messageId = UUID.randomUUID();
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.update(messageId, new MessageUpdateDto("text")))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void deleteMessage_success() {
        // given
        UUID messageId = UUID.randomUUID();
        Message message = mock(Message.class);

        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

        // when
        messageService.delete(messageId);

        // then
        then(messageRepository).should().delete(message);
    }

    @Test
    @DisplayName("메시지 삭제 실패 - 존재하지 않는 메시지")
    void deleteMessage_fail_notFound() {
        // given
        UUID messageId = UUID.randomUUID();
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> messageService.delete(messageId))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("채널 내 메시지 조회 성공")
    void findAllByChannelId_success() {
        // given
        UUID channelId = UUID.randomUUID();
        Pageable pageable = Pageable.ofSize(10);
        Instant createdAt = Instant.now();

        Slice<Message> slice = (Slice<Message>) mock(Slice.class);
        Slice<MessageDto> mappedSlice = (Slice<MessageDto>) mock(Slice.class);
        PageResponse<MessageDto> response = (PageResponse<MessageDto>) mock(PageResponse.class);

        given(messageRepository.findAllByChannelIdWithAuthor(channelId, createdAt, pageable))
                .willReturn(slice);
        given(slice.map(any(Function.class))).willReturn(mappedSlice);
        given(pageResponseMapper.fromSlice(mappedSlice, null)).willReturn(response);
        given(mappedSlice.getContent()).willReturn(List.of());

        // when
        PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, createdAt, pageable);

        // then
        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("채널 내 메시지 조회 실패 - Slice에서 예외 발생")
    void findAllByChannelId_fail_sliceError() {
        // given
        UUID channelId = UUID.randomUUID();
        Pageable pageable = Pageable.ofSize(10);
        Instant createdAt = Instant.now();

        given(messageRepository.findAllByChannelIdWithAuthor(channelId, createdAt, pageable))
                .willThrow(new RuntimeException("DB 실패"));

        // when & then
        assertThatThrownBy(() -> messageService.findAllByChannelId(channelId, createdAt, pageable))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB 실패");
    }
}
