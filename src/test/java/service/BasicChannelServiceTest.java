package service;

import com.sprint.mission.discodeit.dto.service.channel.CreatePrivateChannelCommand;
import com.sprint.mission.discodeit.dto.service.channel.CreatePrivateChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.CreatePublicChannelCommand;
import com.sprint.mission.discodeit.dto.service.channel.CreatePublicChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelResult;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelCommand;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelResult;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.FindReadStatusResult;
import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.ChannelMapperImpl;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserMapperImpl;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BasicChannelServiceTest {

  @Mock
  UserRepository userRepository;

  @Mock
  ChannelRepository channelRepository;

  @Mock
  MessageRepository messageRepository;

  @Mock
  ReadStatusService readStatusService;

  @Spy
  ChannelMapper channelMapper = new ChannelMapperImpl();

  @Spy
  UserMapper userMapper = new UserMapperImpl();

  @InjectMocks
  BasicChannelService basicChannelService;


  @Test
  @DisplayName("공개 채널 생성 성공")
  void create_publicChannel_success() {
    // given
    CreatePublicChannelCommand createPublicChannelCommand = new CreatePublicChannelCommand(
        "publicTest",
        "publicTesting");

    // when
    CreatePublicChannelResult createPublicChannelResult = basicChannelService.createPublicChannel(
        createPublicChannelCommand);

    // then
    assertThat(createPublicChannelCommand.name()).isEqualTo(createPublicChannelResult.name());
    assertThat(createPublicChannelCommand.description()).isEqualTo(
        createPublicChannelResult.description());

    then(channelRepository).should(times(1)).save(any(Channel.class));
  }

  // 입력값 누락으로 인한 실패 테스트 -> Controller 단위 테스트에서 수행

  @Test
  @DisplayName("비밀 채널 생성 성공")
  void create_privateChannel_success() {
    // given
    List<UUID> userIds = List.of(
        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

    // Mapper에서 각 필드가 null이면 예외 발생 -> UserStatus mock 객체 생성
    List<User> mockUsers = userIds.stream()
        .map(id -> {
          User user = mock(User.class);
          UserStatus userStatus = mock(UserStatus.class);
          given(user.getId()).willReturn(id);
          given(user.getUserStatus()).willReturn(userStatus);
          given(userStatus.isLoginUser()).willReturn(true);
          return user;
        })
        .toList();
    CreatePrivateChannelCommand createPrivateChannelCommand = new CreatePrivateChannelCommand(
        userIds);

    given(userRepository.findAllByIdIn(userIds)).willReturn(mockUsers);

    // when
    CreatePrivateChannelResult createPrivateChannelResult = basicChannelService.createPrivateChannel(
        createPrivateChannelCommand);

    // then
    assertThat(createPrivateChannelResult.name()).isNull();
    assertThat(createPrivateChannelResult.description()).isNull();
    assertThat(createPrivateChannelResult.type()).isEqualTo(ChannelType.PRIVATE);

    List<UUID> participantIds = createPrivateChannelResult.participants().stream()
        .map(FindUserResult::id)
        .toList();
    assertThat(participantIds).containsExactlyInAnyOrderElementsOf(userIds);

    then(readStatusService).should(times(3)).create(any(CreateReadStatusCommand.class));
    then(userRepository).should(times(1)).findAllByIdIn(any(List.class));
    then(messageRepository).should(times(1)).findLatestMessageTimeByChannelId(any());
    then(channelRepository).should(times(1)).save(any(Channel.class));
  }

  // 입력값 누락으로 인한 실패 테스트 -> Controller 단위 테스트에서 수행

  @Test
  @DisplayName("userId로 채널 List 찾기 성공")
  void find_allChannels_by_userId_success() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID privateChannelId = UUID.randomUUID();

    FindReadStatusResult readStatusResult1 = new FindReadStatusResult(UUID.randomUUID(), userId,
        channelId, Instant.now());
    FindReadStatusResult readStatusResult2 = new FindReadStatusResult(UUID.randomUUID(), userId,
        privateChannelId, Instant.now());

    Channel publicChannel = Channel.builder()
        .name("publicTest")
        .description("publicTesting")
        .type(ChannelType.PUBLIC)
        .build();
    ReflectionTestUtils.setField(publicChannel, "id", channelId);

    Channel privateChannel = Channel.builder()
        .name("privateTest")
        .description("privateTesting")
        .type(ChannelType.PRIVATE)
        .build();
    ReflectionTestUtils.setField(privateChannel, "id", privateChannelId);

    User user = User.builder()
        .email("testUser@test.com")
        .username("testUser")
        .password("1234")
        .build();
    UserStatus userStatus = new UserStatus(user, Instant.now());
    user.updateUserStatus(userStatus);

    given(readStatusService.findAllByUserId(userId)).willReturn(
        List.of(readStatusResult1, readStatusResult2));
    given(readStatusService.findAllByChannelId(channelId)).willReturn(List.of(readStatusResult1));
    given(channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
        List.of(channelId, privateChannelId))).willReturn(
        List.of(publicChannel, privateChannel));
    given(userRepository.findAllByIdIn(List.of(userId))).willReturn(
        List.of(user));

    // when
    List<FindChannelResult> findChannelResultList = basicChannelService.findAllByUserId(userId);

    // then
    assertThat(2).isEqualTo(findChannelResultList.size());
    assertThat(List.of(privateChannelId, channelId)).containsExactlyInAnyOrderElementsOf(
        findChannelResultList.stream()
            .map(FindChannelResult::id).toList());

    then(readStatusService).should(times(1)).findAllByUserId(any(UUID.class));
    then(channelRepository).should(times(1))
        .findAllByTypeOrIdIn(any(), any(List.class));
    then(readStatusService).should(times(2)).findAllByChannelId(any(UUID.class));
    then(userRepository).should(times(2)).findAllByIdIn(any(List.class));
  }

  @Test
  @DisplayName("공개 채널 수정 성공")
  void updateChannel_public_success() {
    // given
    UUID channelId = UUID.randomUUID();
    UpdateChannelCommand updateChannelCommand = new UpdateChannelCommand("updateTest",
        "updateTesting");

    Channel publicChannel = Channel.builder()
        .name("publicTest")
        .description("publicTesting")
        .type(ChannelType.PUBLIC)
        .build();
    ReflectionTestUtils.setField(publicChannel, "id", channelId);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));

    // when
    UpdateChannelResult updateChannelResult = basicChannelService.update(channelId,
        updateChannelCommand);

    // then
    assertThat(publicChannel.getName()).isEqualTo(updateChannelResult.name());
    assertThat(publicChannel.getDescription()).isEqualTo(updateChannelResult.description());

    then(channelRepository).should(times(1)).findById(any(UUID.class));
  }

  @Test
  @DisplayName("채널 수정할 때, 비밀 채널인 경우 수정 실패")
  void UpdateChannel_private_failed() {
    // given
    UUID privateChannelId = UUID.randomUUID();
    Channel privateChannel = Channel.builder()
        .name("privateTest")
        .description("privateTesting")
        .type(ChannelType.PRIVATE)
        .build();
    ReflectionTestUtils.setField(privateChannel, "id", privateChannelId);
    UpdateChannelCommand updateChannelCommand = new UpdateChannelCommand("updateTest",
        "updateTesting");

    given(channelRepository.findById(privateChannelId)).willReturn(
        Optional.of(privateChannel));

    // when
    assertThatThrownBy(() -> basicChannelService.update(privateChannelId, updateChannelCommand))
        .isInstanceOf(PrivateChannelUpdateException.class)
        .hasMessageContaining("Private channel update is forbidden");

    then(channelRepository).should(times(1)).findById(privateChannelId);
    then(channelRepository).should(never()).save(any(Channel.class));
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void deleteChannel_success() {
    // given
    UUID channelId = UUID.randomUUID();
    Channel publicChannel = Channel.builder()
        .name("publicTest")
        .description("publicTesting")
        .type(ChannelType.PUBLIC)
        .build();
    ReflectionTestUtils.setField(publicChannel, "id", channelId);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));

    // when
    basicChannelService.delete(channelId);

    // then
    then(readStatusService).should(times(1)).deleteByChannelId(channelId);
    then(messageRepository).should(times(1)).deleteByChannelId(channelId);
    then(channelRepository).should(times(1)).deleteById(channelId);
  }

  @Test
  @DisplayName("채널 삭제할 때, 채널이 없는 경우 삭제 실패")
  void deleteChannel_channelNotFound_failed() {
    // given
    UUID channelId = UUID.randomUUID();

    // when
    assertThatThrownBy(() -> basicChannelService.delete(channelId))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessageContaining("Channel not found");

    // then
    then(readStatusService).should(never()).deleteByChannelId(channelId);
    then(messageRepository).should(never()).deleteByChannelId(channelId);
    then(channelRepository).should(never()).deleteById(channelId);
  }
}




