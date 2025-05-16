package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.InvalidMessageAuthorException;
import com.sprint.mission.discodeit.exception.message.InvalidMessageChannelException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasicMessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private BinaryContentService binaryContentService;

    @InjectMocks
    private BasicMessageService messageService;

    @Test
    @DisplayName("createMessage(): 첨부파일 없이 메시지를 정상 생성")
    void createMessage_success_withoutAttachment() {
        // given
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        CreateMessageRequest request = new CreateMessageRequest(userId, channelId, "내용", List.of());

        User user = mock(User.class);
        Channel channel = mock(Channel.class);
        Message message = new Message(user, channel, "내용", List.of());

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(messageRepository.save(any(Message.class))).willReturn(message);

        // when
        Message result = messageService.createMessage(request, null);

        // then
        assertThat(result.getContent()).isEqualTo("내용");
        then(messageRepository).should().save(any(Message.class));
    }

    @Test
    @DisplayName("createMessage(): 작성자 ID가 null이면 예외를 던짐")
    void createMessage_fail_nullAuthorId() {
        // given
        UUID channelId = UUID.randomUUID();
        CreateMessageRequest request = new CreateMessageRequest(null, channelId, "내용", List.of());

        // when & then
        assertThrows(InvalidMessageAuthorException.class, () -> {
            messageService.createMessage(request, null);
        });
    }

    @Test
    @DisplayName("createMessage(): 채널 ID가 null이면 예외를 던짐")
    void createMessage_fail_nullChannelId() {
        // given
        CreateMessageRequest request = new CreateMessageRequest(UUID.randomUUID(), null, "내용",
            List.of());

        // when & then
        assertThrows(InvalidMessageChannelException.class, () -> {
            messageService.createMessage(request, null);
        });
    }

    @Test
    @DisplayName("updateMessage(): 메시지를 정상적으로 수정")
    void updateMessage_success() {
        // given
        UUID messageId = UUID.randomUUID();
        UpdateMessageRequest request = new UpdateMessageRequest("수정된 내용");

        User user = mock(User.class);
        Channel channel = mock(Channel.class);
        Message message = new Message(user, channel, "기존 내용", List.of());

        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
        given(messageMapper.toDto(message)).willReturn(
            new MessageDto(messageId, null, null,
                "수정된 내용", null, null, List.of()));

        // when
        MessageDto result = messageService.updateMessage(messageId, request);

        // then
        assertThat(result.content()).isEqualTo("수정된 내용");
        then(messageRepository).should().findById(messageId);
    }

    @Test
    @DisplayName("updateMessage(): 존재하지 않는 메시지 ID일 경우 예외를 던짐")
    void updateMessage_fail_notFound() {
        // given
        UUID messageId = UUID.randomUUID();
        UpdateMessageRequest request = new UpdateMessageRequest("수정할 내용");

        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when & then
        assertThrows(MessageNotFoundException.class, () -> {
            messageService.updateMessage(messageId, request);
        });
    }

    @Test
    @DisplayName("deleteMessage(): 첨부파일과 함께 메시지를 삭제")
    void deleteMessage_success() {
        UUID messageId = UUID.randomUUID();
        BinaryContent file1 = mock(BinaryContent.class);
        BinaryContent file2 = mock(BinaryContent.class);

        given(file1.getId()).willReturn(UUID.randomUUID());
        given(file2.getId()).willReturn(UUID.randomUUID());

        Message message = mock(Message.class);
        given(message.getAttachments()).willReturn(List.of(file1, file2));

        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

        // when
        messageService.deleteMessage(messageId);

        // then
        then(binaryContentRepository).should().delete(file1);
        then(binaryContentRepository).should().delete(file2);
        then(messageRepository).should().deleteById(messageId);
    }

    @Test
    @DisplayName("deleteMessage(): 존재하지 않는 메시지 ID일 경우 예외를 던짐")
    void deleteMessage_fail_notFound() {
        // given
        UUID messageId = UUID.randomUUID();
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        // when & then
        assertThrows(MessageNotFoundException.class, () -> {
            messageService.deleteMessage(messageId);
        });
    }

    @Test
    @DisplayName("findAllByChannelId(): 커서가 null일 경우 첫 페이지 메시지를 조회")
    void findAllByChannelId_success_firstPage() {
        // given
        UUID channelId = UUID.randomUUID();
        int size = 2;

        Channel channel = mock(Channel.class);
        Message msg1 = mock(Message.class);
        Message msg2 = mock(Message.class);

        MessageDto dto1 = new MessageDto(UUID.randomUUID(), null,
            null, "첫 번째", channelId, null, List.of());
        MessageDto dto2 = new MessageDto(UUID.randomUUID(), null,
            null, "두 번째", channelId, null, List.of());

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(messageRepository.findFirstPageByChannel(eq(channel), any()))
            .willReturn(List.of(msg1, msg2));
        given(messageMapper.toDto(msg1)).willReturn(dto1);
        given(messageMapper.toDto(msg2)).willReturn(dto2);

        // when
        PageResponse<MessageDto> response = messageService.findAllByChannelId(channelId, null,
            size);

        // then
        assertThat(response.content()).hasSize(2);
        assertThat(response.content().get(0).content()).isEqualTo("첫 번째");
        assertThat(response.content().get(1).content()).isEqualTo("두 번째");
    }

    @Test
    @DisplayName("findAllByChannelId(): 존재하지 않는 채널 ID이면 예외를 던짐")
    void findAllByChannelId_fail_channelNotFound() {
        // given
        UUID channelId = UUID.randomUUID();
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        // when & then
        assertThrows(ChannelNotFoundException.class, () -> {
            messageService.findAllByChannelId(channelId, null, 5);
        });
    }
}
