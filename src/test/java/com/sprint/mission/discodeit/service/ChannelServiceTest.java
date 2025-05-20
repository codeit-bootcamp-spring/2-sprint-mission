package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
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

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelMapper channelMapper;

  @InjectMocks
  private BasicChannelService channelService;

  @DisplayName("Private 채널 생성 테스트 - 성공")
  @Test
  void createPrivateChannelSuccess() {
    // given
    UUID userId = UUID.randomUUID();
    CreatePrivateChannelRequest request = new CreatePrivateChannelRequest(List.of(userId));

    Channel channel = new Channel(
        ChannelType.PRIVATE,
        "",
        ""
    );

    User user = mock(User.class);

    ChannelDto channelDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PRIVATE,
        "",
        "",
        List.of(),
        Instant.now()
    );

    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when
    ChannelDto result = channelService.createPrivateChannel(request);

    // then
    assertThat(result).isNotNull();
    assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);
  }

  @DisplayName("Private 채널 생성 테스트 - 존재하지 않는 유저로 실패")
  @Test
  void createPrivateChannelUserNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    CreatePrivateChannelRequest request = new CreatePrivateChannelRequest(List.of(userId));

    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> channelService.createPrivateChannel(request))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining(ErrorCode.USER_NOT_FOUND.name());
  }

  @DisplayName("Public 채널 생성 테스트 - 성공")
  @Test
  void createPublicChannelSuccess() {
    // given
    CreatePublicChannelRequest request = new CreatePublicChannelRequest("public채널", "public채널 설명");

    Channel channel = new Channel(
        ChannelType.PUBLIC,
        "public채널",
        "public채널 설명"
    );

    ChannelDto channelDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        "public채널",
        "public채널 설명",
        List.of(),
        Instant.now()
    );

    given(channelRepository.save(any(Channel.class))).willReturn(channel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when
    ChannelDto result = channelService.createPublicChannel(request);

    // then
    assertThat(result).isNotNull();
    assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
    assertThat(result.name()).isEqualTo(channel.getName());
  }

  @DisplayName("채널 업데이트 테스트 - 성공")
  @Test
  void updateChannelSuccess() {
    // given
    UUID channelId = UUID.randomUUID();
    UpdateChannelRequest request = new UpdateChannelRequest("newName", "newDescription");
    Channel channel = mock(Channel.class);

    ChannelDto channelDto = new ChannelDto(
        UUID.randomUUID(),
        ChannelType.PUBLIC,
        "newName",
        "newDescription",
        List.of(),
        Instant.now()
    );

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    // when

    ChannelDto result = channelService.updateChannel(channelId, request);

    // then
    assertThat(result.name()).isEqualTo(channelDto.name());
  }

  @DisplayName("채널 업데이트 테스트 - Private 채널 업데이트로 실패")
  @Test
  void updateChannel_shouldFail_whenPrivateChannel() {
    // given
    UUID channelId = UUID.randomUUID();
    UpdateChannelRequest request = new UpdateChannelRequest("newName", "newDescription");

    Channel channel = new Channel(
        ChannelType.PRIVATE,
        "",
        ""
    );

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

    // when
    // then
    assertThatThrownBy(() -> channelService.updateChannel(channelId, request))
        .isInstanceOf(PrivateChannelUpdateException.class)
        .hasMessageContaining(ErrorCode.PRIVATE_CHANNEL_UPDATE.name());
  }

  @DisplayName("채널 생성 업데이트 - 존재하지 않는 채널로 실패")
  @Test
  void updateChannelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    UpdateChannelRequest request = new UpdateChannelRequest("newName", "newDescription");

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> channelService.updateChannel(channelId, request))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessageContaining(ErrorCode.CHANNEL_NOT_FOUND.name());
  }

  @DisplayName("채널 삭제 테스트 - 성공")
  @Test
  void deleteChannelSuccess() {
    // given
    UUID channelId = UUID.randomUUID();

    given(channelRepository.existsById(channelId)).willReturn(true);

    // when
    channelService.deleteChannel(channelId);

    // then
    then(channelRepository).should().deleteById(channelId);
  }

  @DisplayName("채널 삭제 테스트 - 존재하지 않는 채널 실패")
  @Test
  void deleteChannelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();

    given(channelRepository.existsById(channelId)).willReturn(false);

    // when
    // then
    assertThatThrownBy(() -> channelService.deleteChannel(channelId))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessageContaining(ErrorCode.CHANNEL_NOT_FOUND.name());
  }


  @DisplayName("유저 채널 조회 테스트 - 성공")
  @Test
  void findAllByUserIdSuccess() {
    // given
    UUID userId = UUID.randomUUID();

    Channel publicChannel = new Channel(ChannelType.PUBLIC, "public", "public desc");
    Channel privateChannel = new Channel(ChannelType.PRIVATE, "", "");

    ReadStatus readStatus = mock(ReadStatus.class);
    given(readStatus.getChannel()).willReturn(privateChannel);

    ChannelDto publicDto = new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "public",
        "public desc", List.of(), Instant.now());
    ChannelDto privateDto = new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, "", "",
        List.of(), Instant.now());

    given(userRepository.existsById(userId)).willReturn(true);
    given(channelRepository.findAllByType(ChannelType.PUBLIC)).willReturn(List.of(publicChannel));
    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));
    given(channelMapper.toDto(publicChannel)).willReturn(publicDto);
    given(channelMapper.toDto(privateChannel)).willReturn(privateDto);

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertThat(result).containsExactlyInAnyOrder(publicDto, privateDto);
    assertThat(result).hasSize(2);
  }

}
