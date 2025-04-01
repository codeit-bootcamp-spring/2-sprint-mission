package com.sprint.mission.discodeit.service;

import static com.sprint.mission.discodeit.util.mock.channel.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.util.mock.user.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.dto.readstatus.ReadStatusResult;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReadStatusServiceTest {

  private BasicReadStatusService basicReadStatusService;
  private User setUpUser;
  private Channel setUpPublicChannel;

  @BeforeEach
  void setUp() {
    UserRepository userRepository = new JCFUserRepository();
    ChannelRepository channelRepository = new JCFChannelRepository();
    ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();
    basicReadStatusService = new BasicReadStatusService(readStatusRepository, channelRepository,
        userRepository);
    setUpUser = userRepository.save(
        new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
    setUpPublicChannel = channelRepository.save(
        new Channel(ChannelType.PUBLIC, CHANNEL_NAME));
  }

  @DisplayName("읽기 상태를 생성하면 해당 읽기 상태 객체가 반환됩니다.")
  @Test
  void createReadStatus() {
    ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(setUpUser.getId(),
        setUpPublicChannel.getId());
    ReadStatusResult readStatus = basicReadStatusService.create(readStatusCreateRequest);

    assertThat(readStatus.readStatusId()).isNotNull();
  }

  @DisplayName("존재하지 않는 유저 ID로 읽기 상태 생성 시 예외를 반환합니다.")
  @Test
  void createReadStatusWithNonUser() {
    ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(UUID.randomUUID(),
        setUpPublicChannel.getId());

    assertThatThrownBy(() -> basicReadStatusService.create(readStatusCreateRequest))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("존재하지 않는 채널 ID로 읽기 상태 생성 시 예외를 반환합니다.")
  @Test
  void createReadStatusWithNoChannel() {
    ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(setUpUser.getId(),
        UUID.randomUUID());

    assertThatThrownBy(() -> basicReadStatusService.create(readStatusCreateRequest))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("이미 존재하는 읽기 상태를 생성하려 하면 예외 발생")
  @Test
  void createReadStatusWhenAlreadyExists() {
    ReadStatusCreateRequest readStatusCreateRequest = new ReadStatusCreateRequest(setUpUser.getId(),
        setUpPublicChannel.getId());

    basicReadStatusService.create(readStatusCreateRequest);
    assertThatThrownBy(() -> basicReadStatusService.create(readStatusCreateRequest))
        .isInstanceOf(IllegalArgumentException.class);
  }
}