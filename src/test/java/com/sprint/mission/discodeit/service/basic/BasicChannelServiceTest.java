package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelModifyException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

    @InjectMocks
    BasicChannelService channelService;

    @Mock
    ChannelRepository channelRepository;

    @Mock
    ReadStatusRepository readStatusRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ChannelMapper channelMapper;

    @Test
    void 비공개_채널_저장_테스트() {
        // given
        given(channelRepository.save(any(Channel.class)))
            .willAnswer(invocation -> invocation.<Channel>getArgument(0));

        // when
        channelService.createPrivateChannel(new PrivateChannelCreateRequest(List.of()));

        // then
        then(channelRepository).should().save(argThat(channel ->
            channel.getType() == ChannelType.PRIVATE
        ));
    }

    @Test
    void 비공개_채널_저장_시_읽음_상태_저장_테스트() {
        // given
        User user = new User();
        Channel channel = new Channel();
        given(channelRepository.save(any(Channel.class))).willReturn(channel);
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        // when
        channelService.createPrivateChannel(
            new PrivateChannelCreateRequest(List.of(UUID.randomUUID(), UUID.randomUUID())));

        // then
        then(readStatusRepository).should(times(2)).save(any(ReadStatus.class));
    }

    @Test
    void 비공개_채널_저장_시_사용자_찾을_수_없음_테스트() {
        // given
        UUID userId = UUID.randomUUID();

        // when & then
        assertThrows(UserNotFoundException.class, () -> {
            channelService.createPrivateChannel(
                new PrivateChannelCreateRequest(List.of(userId))
            );
        });
    }

    @Test
    void 공개_채널_수정_테스트() {
        // given
        Channel channel = new Channel(null, null, ChannelType.PUBLIC);
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("test", "test");

        given(channelRepository.findById(any())).willReturn(Optional.of(channel));

        // when
        channelService.updateChannel(UUID.randomUUID(), request);

        // then
        assertThat(channel.getName()).isEqualTo("test");
        assertThat(channel.getDescription()).isEqualTo("test");
    }

    @Test
    void 비공개_채널_수정_테스트() {
        // given
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("test", "test");

        given(channelRepository.findById(any())).willReturn(Optional.of(channel));

        // when & then
        assertThrows(PrivateChannelModifyException.class, () -> {
            channelService.updateChannel(UUID.randomUUID(), request);
        });
    }

    @Test
    void 채널_삭제_테스트() {
        // given
        given(channelRepository.findById(any())).willReturn(Optional.of(new Channel()));

        // when
        channelService.deleteChannel(UUID.randomUUID());

        // then
        then(channelRepository).should(times(1)).delete(any(Channel.class));
        then(messageRepository).should(times(1)).deleteAll(anyList());
        then(readStatusRepository).should(times(1)).deleteAll(anyList());
    }

    @Test
    void 채널_삭제_시_사용자_찾을_수_없음_테스트() {
        // given
        given(channelRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThrows(ChannelNotFoundException.class, () -> {
            channelService.deleteChannel(UUID.randomUUID());
        });

        then(channelRepository).should(times(0)).delete(any(Channel.class));
        then(messageRepository).should(times(0)).deleteAll(anyList());
        then(readStatusRepository).should(times(0)).deleteAll(anyList());
    }

}