package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import com.sprint.mission.discodeit.core.channel.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.core.channel.exception.ChannelUnmodifiableException;
import com.sprint.mission.discodeit.core.channel.repository.JpaChannelRepository;
import com.sprint.mission.discodeit.core.channel.usecase.BasicChannelService;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelDto;
import com.sprint.mission.discodeit.core.channel.usecase.dto.ChannelUpdateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PrivateChannelCreateCommand;
import com.sprint.mission.discodeit.core.channel.usecase.dto.PublicChannelCreateCommand;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.message.repository.JpaMessageRepository;
import com.sprint.mission.discodeit.core.status.entity.ReadStatus;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.repository.JpaReadStatusRepository;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceUnitTest {

  @Mock
  private JpaUserRepository userRepository;

  @Mock
  private JpaChannelRepository channelRepository;

  @Mock
  private JpaMessageRepository messageRepository;

  @Mock
  private JpaReadStatusRepository readStatusRepository;

  @InjectMocks
  private BasicChannelService channelService;

  @Test
  void PublicChannelCreate() {
    // given
    PublicChannelCreateCommand command = new PublicChannelCreateCommand("abc", "abc");
    // when
    ChannelDto channelDto = channelService.create(command);
    // then
    assertThat(channelDto.name()).isEqualTo("abc");
    assertThat(channelDto.description()).isEqualTo("abc");
    assertThat(channelDto.type()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  void PrivateChannelCreateSuccess() {
    // given
    UUID userId = UUID.randomUUID();
    BinaryContent oldProfile = BinaryContent.create("old.png", 0L, "image/png");
    User user = User.create("a", "a@email.com", "a", oldProfile);
    user.setUserStatus(UserStatus.create(user, Instant.now()));

    PrivateChannelCreateCommand command = new PrivateChannelCreateCommand(
        List.of(userId));
    when(userRepository.findAllById(List.of(userId))).thenReturn(List.of(user));
    // when
    ChannelDto channelDto = channelService.create(command);
    // then
    assertThat(channelDto.name()).isNull();
    assertThat(channelDto.description()).isNull();
    assertThat(channelDto.type()).isEqualTo(ChannelType.PRIVATE);
  }

  @Test
  void ChannelUpdateSuccess() {
    // given
    Channel channel = Channel.create("test", "test", ChannelType.PUBLIC);
    UUID channelUUID = UUID.randomUUID();
    ChannelUpdateCommand channelUpdateCommand = new ChannelUpdateCommand(channelUUID, "aaa", "aaa");
    when(channelRepository.findById(channelUUID)).thenReturn(Optional.of(channel));
    // when
    ChannelDto update = channelService.update(channelUpdateCommand);
    // then
    assertThat(update.name()).isEqualTo("aaa");
    assertThat(update.description()).isEqualTo("aaa");
    assertThat(update.type()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  void ChannelUpdate_WithoutChannel_ShouldThrowException() {
    // given
    UUID channelUUID = UUID.randomUUID();
    ChannelUpdateCommand channelUpdateCommand = new ChannelUpdateCommand(channelUUID, "aaa", "aaa");
    when(channelRepository.findById(channelUUID)).thenReturn(Optional.empty());
    // when & then
    assertThrows(ChannelNotFoundException.class, () -> {
      channelService.update(channelUpdateCommand);
    });
  }

  @Test
  void ChannelUpdate_WithPrivateChannel_ShouldThrowException() {
    // given
    Channel channel = Channel.create("test", "test", ChannelType.PRIVATE);
    UUID channelUUID = UUID.randomUUID();
    ChannelUpdateCommand channelUpdateCommand = new ChannelUpdateCommand(channelUUID, "aaa", "aaa");
    when(channelRepository.findById(channelUUID)).thenReturn(Optional.of(channel));
    // when & then
    assertThrows(ChannelUnmodifiableException.class, () -> {
      channelService.update(channelUpdateCommand);
    });
  }

  @Test
  void ChannelDeleteSuccess() {
    // given
    UUID channelId = UUID.randomUUID();
    Channel spyChannel = spy(Channel.class);
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(spyChannel));
    // when
    channelService.delete(channelId);
    // then
    verify(channelRepository).delete(spyChannel);
  }

  @Test
  void ChannelDelete_WithoutChannel_ShouldThrowException() {
    // given
    UUID channelId = UUID.randomUUID();
    when(channelRepository.findById(channelId)).thenReturn(Optional.empty());
    // when & then
    assertThrows(ChannelNotFoundException.class, () -> channelService.delete(channelId));

    verify(channelRepository, never()).delete(any());
  }

  @Test
  void ChannelFindAllByUserIdSuccess() {
    // given
    UUID userId = UUID.randomUUID();

    UUID channelId1 = UUID.randomUUID();
    UUID channelId2 = UUID.randomUUID();
    List<UUID> channelIds = List.of(channelId1, channelId2);
    Channel channel1 = mock(Channel.class);
    Channel channel2 = mock(Channel.class);

    ReadStatus rs1 = mock(ReadStatus.class);
    ReadStatus rs2 = mock(ReadStatus.class);

    List<ReadStatus> readStatusList = List.of(rs1, rs2);
    List<Channel> accessibleChannels = List.of(channel1, channel2);

    when(channel1.getId()).thenReturn(channelId1);
    when(channel2.getId()).thenReturn(channelId2);
    when(rs1.getChannel()).thenReturn(channel1);
    when(rs2.getChannel()).thenReturn(channel2);

    when(readStatusRepository.findAllByUser_Id(userId)).thenReturn(readStatusList);
    when(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, channelIds))
        .thenReturn(accessibleChannels);
    // when
    List<ChannelDto> results = channelService.findAllByUserId(userId);
    // then
    assertThat(results.size()).isEqualTo(2);
    verify(readStatusRepository).findAllByUser_Id(userId);
    verify(channelRepository).findAllByTypeOrIdIn(ChannelType.PUBLIC, channelIds);
  }
}
