package com.sprint.mission.discodeit.service;

import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.UPDATED_CHANNEL_NAME;
import static com.sprint.mission.discodeit.util.mock.message.MessageInfo.MESSAGE_CONTENT;
import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.LOGIN_USER;
import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.OTHER_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.application.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.application.dto.channel.ChannelResult;
import com.sprint.mission.discodeit.application.dto.channel.PrivateChannelCreationRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChannelServiceTest {

  private ChannelService channelService;
  private UserRepository userRepository;
  private MessageRepository messageRepository;
  private ReadStatusRepository readStatusRepository;
  private ChannelResult setUpPublicChannel;
  private User setUpUser;

  @BeforeEach
  void setUp() {
    messageRepository = new JCFMessageRepository();
    readStatusRepository = new JCFReadStatusRepository();
    userRepository = new JCFUserRepository();
    channelService = new BasicChannelService(new JCFChannelRepository(), readStatusRepository,
        messageRepository);

    setUpUser = userRepository.save(
        new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
    setUpPublicChannel = channelService.createPublic(new ChannelCreateRequest(CHANNEL_NAME,
        setUpUser.getId()));
  }

  @DisplayName("Public 채널 생성 시 채널 이름과 정보를 반환합니다.")
  @Test
  void createPublicChannel() {
    assertThat(setUpPublicChannel.name()).isEqualTo(CHANNEL_NAME);
  }

  @DisplayName("Public 채널 생성 시 멤버는 null을 반환합니다.")
  @Test
  void createPublicChannel_NoMember() {
    assertThat(setUpPublicChannel.privateMemberIds()).isNull();
  }

  @DisplayName("Private 채널 생성 시 채널 멤버도 같이 반홥합니다.")
  @Test
  void createPrivateChannel() {
    PrivateChannelCreationRequest privateChannelCreationRequest = new PrivateChannelCreationRequest(
        CHANNEL_NAME, setUpUser.getId(), List.of());
    ChannelResult privateChannel = channelService.createPrivate(privateChannelCreationRequest);

    assertThat(privateChannel.privateMemberIds()).containsExactlyInAnyOrder(setUpUser.getId());
  }

  @DisplayName("채널 ID로 조회하면 해당하는 채널을 반환합니다.")
  @Test
  void getChannelById() {
    ChannelResult channel = channelService.getById(setUpPublicChannel.id());

    assertThat(setUpPublicChannel.id()).isEqualTo(channel.id());
  }

  @DisplayName("채널을 조회하면 가장 최근 메시지의 생성 시간을 반환합니다.")
  @Test
  void getChannelByIdReturnsLastMessageTimestamp() {
    Message message = messageRepository.save(
        new Message(MESSAGE_CONTENT, setUpPublicChannel.id(), setUpUser.getId(),
            List.of()));

    ChannelResult channel = channelService.getById(setUpPublicChannel.id());
    assertThat(channel.lastMessageCreatedAt()).isEqualTo(message.getCreatedAt());
  }


  @DisplayName("유저 Id로 채널을 조회한다면 Public 채널과 유저에게만 속한 Private 채널만 반환합니다.")
  @Test
  void getAll() {
    PrivateChannelCreationRequest privateChannelCreationRequest = new PrivateChannelCreationRequest(
        CHANNEL_NAME, setUpUser.getId(), List.of());
    ChannelResult privateChannel = channelService.createPrivate(privateChannelCreationRequest);

    List<UUID> channelIds = channelService.getAllByUserId(setUpUser.getId())
        .stream()
        .map(ChannelResult::id)
        .toList();

    assertThat(channelIds).containsExactlyInAnyOrder(privateChannel.id(), setUpPublicChannel.id());
  }

  @DisplayName("Private 채널에 맴버를 추가하면 추가된 멤버들의 Id를 반환합니다.")
  @Test
  void getByIdPrivateChannel_MemberTogether() {
    User friend = userRepository.save(
        new User(OTHER_USER.getName(), OTHER_USER.getEmail(), OTHER_USER.getPassword(), null));
    PrivateChannelCreationRequest privateChannelCreationRequest = new PrivateChannelCreationRequest(
        CHANNEL_NAME, setUpUser.getId(), List.of());
    ChannelResult privateChannel = channelService.createPrivate(privateChannelCreationRequest);

    ChannelResult addedPrivateChannel = channelService.addPrivateChannelMember(privateChannel.id(),
        friend.getId());

    assertThat(addedPrivateChannel.privateMemberIds()).containsExactlyInAnyOrder(setUpUser.getId(),
        friend.getId());
  }

  @DisplayName("전체 채널을 조회한다면 공개 채널과 사용자가 속한 비공개 채널만 반환합니다.")
  @Test
  void getAllChannelsForUser() {
    User otherUser = userRepository.save(
        new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

    channelService.createPrivate(
        new PrivateChannelCreationRequest(CHANNEL_NAME, otherUser.getId(), List.of()));

    ChannelResult userPrivateChannel = channelService.createPrivate(
        new PrivateChannelCreationRequest(CHANNEL_NAME, setUpUser.getId(), List.of()));

    List<UUID> setUpUserChannelIds = channelService.getAllByUserId(setUpUser.getId())
        .stream()
        .map(ChannelResult::id)
        .toList();

    assertThat(setUpUserChannelIds).containsExactlyInAnyOrder(setUpPublicChannel.id(),
        userPrivateChannel.id());
  }

  @DisplayName("Public 채널의 이름을 업데이트하면 변경된 정보가 반환합니다.")
  @Test
  void updatePublicChannelName() {
    channelService.updatePublicChannelName(setUpPublicChannel.id(), UPDATED_CHANNEL_NAME);

    assertThat(channelService.getById(setUpPublicChannel.id()).name()).isEqualTo(
        UPDATED_CHANNEL_NAME);
  }

  @DisplayName("Private 채널의 이름을 업데이트 시도 한다면 예외를 반환합니다.")
  @Test
  void updatePrivateChannelNameThrowsException() {
    PrivateChannelCreationRequest privateChannelDto = new PrivateChannelCreationRequest(
        CHANNEL_NAME, setUpUser.getId(), List.of());
    ChannelResult privateChannel = channelService.createPrivate(privateChannelDto);

    assertThatThrownBy(
        () -> channelService.updatePublicChannelName(privateChannel.id(), UPDATED_CHANNEL_NAME))
        .isInstanceOf(IllegalArgumentException.class);
  }


  @DisplayName("채널 삭제 시 조회한다면 예외를 반환합니다.")
  @Test
  void deleteChannelRemovesMessagesAndReadStatus() {
    UUID channelId = setUpPublicChannel.id();
    channelService.delete(channelId);

    assertThatThrownBy(() -> channelService.getById(channelId)).isInstanceOf(
        IllegalArgumentException.class);
  }

  @DisplayName("채널 삭제 시 채널에 속한 메시지도 함께 삭제됩니다.")
  @Test
  void deleteChannelRemovesMessages() {
    UUID channelId = setUpPublicChannel.id();
    channelService.delete(channelId);

    boolean isExisting = messageRepository.findAll()
        .stream()
        .anyMatch(message -> message.getChannelId().equals(channelId));

    assertThat(isExisting).isFalse();
  }

  @DisplayName("비공개 채널 삭제 시 읽음 상태도 함께 삭제됩니다.")
  @Test
  void deletePrivateChannelRemovesReadStatus() {
    ChannelResult privateChannel = channelService.createPrivate(
        new PrivateChannelCreationRequest(CHANNEL_NAME, setUpUser.getId(), List.of()));

    UUID privateChannelId = privateChannel.id();
    channelService.delete(privateChannelId);

    boolean isExisting = readStatusRepository.findByChannelId(privateChannelId)
        .stream()
        .anyMatch(status -> status.getChannelId().equals(privateChannelId));

    assertThat(isExisting).isFalse();
  }
}