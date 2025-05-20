package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    MessageRepository messageRepository;
    @Mock
    ChannelRepository channelRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BinaryContentRepository binaryContentRepository;
    @Mock
    MessageMapper messageMapper;
    @Mock
    BinaryContentStorage binaryContentStorage;
    @Mock
    PageResponseMapper pageResponseMapper;

    @InjectMocks
    BasicMessageService messageService;

    @Test
    void create_success() {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        String content = "테스트 메시지";
        Channel channel = mock(Channel.class);
        User author = mock(User.class);

        MessageCreateRequest req = new MessageCreateRequest(content, channelId, authorId);

        BinaryContentCreateRequest fileReq = new BinaryContentCreateRequest(
                "file.txt", "text/plain", "hello".getBytes());
        BinaryContent savedFile = new BinaryContent("file.txt", 5L, "text/plain");

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(authorId)).willReturn(Optional.of(author));
        given(binaryContentRepository.save(any())).willReturn(savedFile);

        Message message = new Message(content, channel, author, List.of(savedFile));
        given(messageRepository.save(any(Message.class))).willReturn(message);

        MessageDto dto = new MessageDto(
                UUID.randomUUID(),
                null, null,
                content,
                channelId,
                mock(UserDto.class),
                List.of()
        );
        given(messageMapper.toDto(any())).willReturn(dto);

        // when
        MessageDto result = messageService.create(req, List.of(fileReq));

        // then
        assertThat(result.content()).isEqualTo(content);
        verify(channelRepository).findById(channelId);
        verify(userRepository).findById(authorId);
        verify(binaryContentRepository).save(any());
        verify(binaryContentStorage).put(any(), any());
        verify(messageRepository).save(any());
        verify(messageMapper).toDto(any());
    }

    @Test
    void create_fail_noChannel() {
        // given
        UUID channelId = UUID.randomUUID();
        MessageCreateRequest req = new MessageCreateRequest("hi", channelId, UUID.randomUUID());

        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> messageService.create(req, List.of()))
                .isInstanceOf(ChannelNotFoundException.class);
    }

}
