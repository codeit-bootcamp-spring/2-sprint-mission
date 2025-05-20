package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ParticipantUserNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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
public class BasicChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ReadStatusRepository readStatusRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService channelService;

    @Test
    @DisplayName("createPublicChannel(): 정상적으로 공개 채널을 생성")
    void createPublicChannel_success() {
        // given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지사항", "회사 공지 채널");
        Channel channel = new Channel("공지사항", "회사 공지 채널", ChannelType.PUBLIC);
        given(channelRepository.save(any(Channel.class))).willReturn(channel);
        given(channelMapper.toDto(any(Channel.class))).willReturn(
            new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "공지사항",
                "회사 공지 채널", List.of(), null)
        );

        // when
        ChannelDto result = channelService.createPublicChannel(request);

        // then
        assertThat(result.name()).isEqualTo("공지사항");
        then(channelRepository).should().save(any(Channel.class));
    }

    @Test
    @DisplayName("createPrivateChannel(): 사용자가 존재할때 정상적으로 비공개 채널을 생성")
    void createPrivateChannel_success() {
        // given
        UUID userId = UUID.randomUUID();
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(userId));
        Channel channel = new Channel("PRIVATE CHANNEL", "비공개 채널입니다.", ChannelType.PRIVATE);
        User user = mock(User.class);

        given(channelRepository.save(any(Channel.class))).willReturn(channel);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(channelMapper.toDto(any(Channel.class))).willReturn(
            new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, "PRIVATE CHANNEL", "비공개 채널입니다.",
                List.of(), null)
        );

        // when
        ChannelDto result = channelService.createPrivateChannel(request);

        // then
        assertThat(result.name()).isEqualTo("PRIVATE CHANNEL");
        then(userRepository).should().findById(userId);
        then(readStatusRepository).should().save(any());
    }

    @Test
    @DisplayName("createPrivateChannel(): 존재하지 않는 사용자가 포함되면 예외를 던진다")
    void createPrivateChannel_fail_participantNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(userId));
        Channel channel = new Channel("PRIVATE CHANNEL", "비공개 채널입니다.", ChannelType.PRIVATE);

        given(channelRepository.save(any(Channel.class))).willReturn(channel);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThrows(ParticipantUserNotFoundException.class, () -> {
            channelService.createPrivateChannel(request);
        });
    }

    @Test
    @DisplayName("updateChannel(): 공개 채널을 정상적으로 수정")
    void updateChannel_success() {
        // given
        UUID channelId = UUID.randomUUID();
        UpdateChannelRequest request = new UpdateChannelRequest("newName", "newDesc");
        Channel channel = new Channel("oldName", "oldDesc", ChannelType.PUBLIC);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(channelMapper.toDto(any(Channel.class))).willReturn(
            new ChannelDto(channelId, ChannelType.PUBLIC, "newName", "newDesc", List.of(), null)
        );

        // when
        ChannelDto result = channelService.updateChannel(channelId, request);

        // then
        assertThat(result.name()).isEqualTo("newName");
        then(channelRepository).should().findById(channelId);
    }

    @Test
    @DisplayName("updateChannel(): 비공개 채널은 수정할 수 없어 예외를 던짐")
    void updateChannel_fail_privateChannel() {
        // given
        UUID channelId = UUID.randomUUID();
        UpdateChannelRequest request = new UpdateChannelRequest("newName", "newDesc");
        Channel channel = new Channel("private", "desc", ChannelType.PRIVATE);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        // when & then
        assertThrows(PrivateChannelUpdateException.class, () -> {
            channelService.updateChannel(channelId, request);
        });
    }

    @Test
    @DisplayName("updateChannel(): 존재하지 않는 채널 ID는 예외를 던짐")
    void updateChannel_fail_channelNotFound() {
        // given
        UUID channelId = UUID.randomUUID();
        UpdateChannelRequest request = new UpdateChannelRequest("newName", "newDesc");

        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        // when & then
        assertThrows(ChannelNotFoundException.class, () -> {
            channelService.updateChannel(channelId, request);
        });
    }

    @Test
    @DisplayName("deleteChannel(): 존재하는 채널을 삭제")
    void deleteChannel_success() {
        // given
        UUID channelId = UUID.randomUUID();

        given(channelRepository.existsById(channelId)).willReturn(true);

        // when
        channelService.deleteChannel(channelId);

        // then
        then(messageRepository).should().deleteAllByChannelId(channelId);
        then(readStatusRepository).should().deleteAllByChannelId(channelId);
        then(channelRepository).should().deleteById(channelId);
    }

    @Test
    @DisplayName("deleteChannel(): 존재하지 않는 채널 ID면 예외를 던짐")
    void deleteChannel_fail_channelNotFound() {
        // given
        UUID channelId = UUID.randomUUID();
        given(channelRepository.existsById(channelId)).willReturn(false);

        // when & then
        assertThrows(ChannelNotFoundException.class, () -> {
            channelService.deleteChannel(channelId);
        });
    }
}
