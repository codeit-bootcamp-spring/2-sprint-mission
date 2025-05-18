package com.sprint.mission.discodeit.service.test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/*
MessageService : create, update, delete, findByChannelId의 성공, 실패 testCase
 */
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    MessageRepository messageRepository;
    @Mock
    ChannelRepository channelRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    MessageMapper messageMapper;
    @Mock
    BinaryContentRepository binaryContentRepository;
    @Mock
    BinaryContentStorage binaryContentStorage;
    @Mock
    PageResponseMapper pageResponseMapper;

    @InjectMocks
    BasicMessageService messageService;

    @Test
    @DisplayName("MessageCreateTest_success")
    void MessageCreateTest_success() {
        UUID messageId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        UUID contentId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest("testMessage", channelId, userId);
        BinaryContentCreateRequest contentCreateRequest = new BinaryContentCreateRequest(
            "test.txt", "text/plain", "내용내용".getBytes(StandardCharsets.UTF_8));

        BinaryContent binaryContent = new BinaryContent("test.txt", 10L, "내용내용");
        ReflectionTestUtils.setField(binaryContent, "id", contentId);

        Channel channel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");
        User user = new User("testName", "test@naver.com", "password", null);
        Message message = new Message("내용내용", channel, user, List.of(binaryContent));
        ReflectionTestUtils.setField(message, "id", messageId);

        UserDto userDto = new UserDto(userId, "testName", "test@naver.com", null, false);
        BinaryContentDto attachmentDto = new BinaryContentDto(contentId, "test.txt", 10L, "내용내용");
        MessageDto messageDto = new MessageDto(
            messageId, Instant.now(), Instant.now(), "내용내용", channelId, userDto,
            List.of(attachmentDto));

        // given
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(binaryContentRepository.save(any())).willReturn(binaryContent);
        given(messageRepository.save(any())).willReturn(message);
        given(messageMapper.toDto(message)).willReturn(messageDto);

        // when
        MessageDto result = messageService.create(request, List.of(contentCreateRequest));

        // then
        assertThat(result).isEqualTo(messageDto);
        then(channelRepository).should().findById(channelId);
        then(userRepository).should().findById(userId);
        then(binaryContentRepository).should().save(any());
        then(messageRepository).should().save(any());
        then(binaryContentStorage).should().put(eq(contentId), any());
        then(messageRepository).should().save(any(Message.class));
    }

    @Test
    @DisplayName("MessageCreateTest_fail_notFound")
    void MessageCreateTest_fail_notFound() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest("내용내용", channelId, authorId);

        given(channelRepository.findById(channelId))
            .willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> messageService.create(request, List.of()))
            .isInstanceOf(ChannelNotFoundException.class);

        then(channelRepository).should().findById(channelId);
        then(userRepository).should(never()).findById(any());
        then(messageRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("MessageUpdateTest_success")
    void MessageUpdateTest_success() {
        // given
        UUID messageId = UUID.randomUUID();
        Instant createdAt = Instant.now().minusSeconds(60);
        Instant updatedAt = Instant.now();

        MessageUpdateRequest request = new MessageUpdateRequest("수정된 메시지");

        Channel channel = new Channel(ChannelType.PUBLIC, "공지", "공지사항");
        User author = new User("작성자", "email@test.com", "pw", null);

        Message message = new Message("기존 메시지", channel, author, List.of());
        ReflectionTestUtils.setField(message, "id", messageId);
        ReflectionTestUtils.setField(message, "createdAt", createdAt);
        ReflectionTestUtils.setField(message, "updatedAt", updatedAt);

        UserDto authorDto = new UserDto(UUID.randomUUID(), "작성자", "email@test.com", null, false);

        MessageDto expected = new MessageDto(
            messageId,
            createdAt,
            updatedAt,
            "수정된 메시지",
            channel.getId(),
            authorDto,
            List.of()
        );

        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
        given(messageMapper.toDto(message)).willReturn(expected);

        // when
        MessageDto result = messageService.update(messageId, request);

        // then
        assertThat(result).isEqualTo(expected);
        then(messageRepository).should().findById(messageId);
    }

    @Test
    @DisplayName("MessageUpdateTest_fail_notFound")
    void MessageUpdateTest_fail_notFound() {
        // given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("수정 내용");

        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> messageService.update(messageId, request))
            .isInstanceOf(DiscodeitException.class);

        then(messageRepository).should().findById(messageId);
        then(messageMapper).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("MessageDeleteTest_success")
    void deleteMessage_success() {
        // given
        UUID messageId = UUID.randomUUID();
        given(messageRepository.existsById(messageId)).willReturn(true);

        // when
        messageService.delete(messageId);

        // then
        then(messageRepository).should().existsById(messageId);
        then(messageRepository).should().deleteById(messageId);
    }

    @Test
    @DisplayName("MessageDeleteTest_fail_notFound")
    void deleteMessage_fail_ifNotFound() {
        // given
        UUID messageId = UUID.randomUUID();
        given(messageRepository.existsById(messageId)).willReturn(false);

        // when / then
        assertThatThrownBy(() -> messageService.delete(messageId))
            .isInstanceOf(DiscodeitException.class)
            .hasMessageContaining("Meesage is not exist");

        then(messageRepository).should().existsById(messageId);
        then(messageRepository).should(never()).deleteById(any());
    }

    @Test
    @DisplayName("MessageFindAllByChannelIdTest_success")
    void findAllByChannelId_success() {
        // given
        UUID channelId = UUID.randomUUID();
        Instant now = Instant.now();
        Pageable pageable = PageRequest.of(0, 10);

        Message message = mock(Message.class);
        MessageDto dto = mock(MessageDto.class);
        Slice<Message> slice = new SliceImpl<>(List.of(message), pageable, false);

        // 타입을 보존한 mock 인스턴스 생성
        @SuppressWarnings("unchecked")
        PageResponse<MessageDto> expectedPage = (PageResponse<MessageDto>) mock(PageResponse.class);

        given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable)))
            .willReturn(slice);
        given(messageMapper.toDto(message)).willReturn(dto);
        // stub 할 때 제네릭 파라미터를 명확히 제시 -> expectedPage에 빨간줄 없어짐
        given(pageResponseMapper.fromSlice(any(Slice.class), any())).willReturn(expectedPage);

        // when
        PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, now,
            pageable);

        // then
        assertThat(result).isEqualTo(expectedPage);
        then(messageRepository).should()
            .findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable));
        then(messageMapper).should().toDto(message);
        then(pageResponseMapper).should().fromSlice(any(), any());
    }


}
