package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    @InjectMocks
    private BasicMessageService messageService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ChannelRepository channelRepository;

    @Mock
    MessageMapper messageMapper;

    @Test
    void 메세지_생성_테스트() {
        // given
        MessageCreateRequest request = new MessageCreateRequest(UUID.randomUUID(),
            UUID.randomUUID(), "test");
        given(userRepository.findById(any())).willReturn(Optional.of(new User()));
        given(channelRepository.findById(any())).willReturn(Optional.of(new Channel()));

        // when
        messageService.sendMessage(request, null);

        // then
        then(messageRepository).should().save(any());
    }

    @Test
    void 메세지_생성_시_채널_찾을_수_없음_테스트() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest(channelId,
            authorId, "test");

        given(userRepository.findById(authorId)).willReturn(Optional.of(new User()));
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        // when & then
        assertThrows(ChannelNotFoundException.class, () -> {
            messageService.sendMessage(request, null);
        });
    }

    @Test
    void 메세지_생성_시_사용자_찾을_수_없음_테스트() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        MessageCreateRequest request = new MessageCreateRequest(channelId,
            authorId, "test");

        given(userRepository.findById(authorId)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> {
            messageService.sendMessage(request, null);
        });
    }

    @Test
    void 메세지_수정_테스트() {
        // given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = new MessageUpdateRequest("test");
        Message message = new Message();
        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

        // when
        messageService.updateMessage(messageId, request);

        // then
        assertThat(message.getContent()).isEqualTo("test");
    }

    @Test
    void 메세지_수정_중_메세지_아이디_찾을_수_없음_테스트() {
        // given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest request = Mockito.mock(MessageUpdateRequest.class);
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when & then
        assertThrows(MessageNotFoundException.class, () -> {
            messageService.updateMessage(messageId, request);
        });
    }

    @Test
    void 메세지_삭제_테스트() {
        // given
        UUID messageId = UUID.randomUUID();
        given(messageRepository.findById(messageId)).willReturn(Optional.of(new Message()));

        // when
        messageService.deleteMessageById(messageId);

        // then
        then(messageRepository).should().delete(any());
    }

    @Test
    void 메세지_삭제_중_메세지_찾을_수_없음() {
        // given
        UUID messageId = UUID.randomUUID();

        // when & then
        assertThrows(MessageNotFoundException.class, () -> {
            messageService.deleteMessageById(messageId);
        });
    }
}