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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReadStatusServiceTest {

  private UserRepository userRepository;
  private ChannelRepository channelRepository;
  private ReadStatusRepository readStatusRepository;
  private BasicReadStatusService basicReadStatusService;
  private User user;
  private Channel channel;

  private ReadStatusCreateRequest readStatusCreateRequest;

  @BeforeEach
  void setUp() {
    userRepository = new JCFUserRepository();
    channelRepository = new JCFChannelRepository();
    readStatusRepository = new JCFReadStatusRepository();
    basicReadStatusService = new BasicReadStatusService(readStatusRepository, channelRepository,
        userRepository);
    user = userRepository.save(
        new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));
    channel = channelRepository.save(new Channel(ChannelType.PUBLIC, CHANNEL_NAME));
    readStatusCreateRequest = new ReadStatusCreateRequest(user.getId(), channel.getId(), null);
  }

  @DisplayName("읽기 상태를 생성하면 ID가 반환된다.")
  @Test
  void createReadStatus() {
    ReadStatusResult readStatus = basicReadStatusService.create(readStatusCreateRequest);
    assertThat(readStatus.readStatusId()).isNotNull();
  }

  @DisplayName("존재하지 않는 유저 ID로 읽기 상태 생성 시 예외 발생")
  @Test
  void createReadStatusWithNonUser() {
    assertThatThrownBy(() -> basicReadStatusService.create(readStatusCreateRequest))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("존재하지 않는 채널 ID로 읽기 상태 생성 시 예외 발생")
  @Test
  void createReadStatusWithNoChannel() {
    assertThatThrownBy(() -> basicReadStatusService.create(readStatusCreateRequest))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("이미 존재하는 읽기 상태를 생성하려 하면 예외 발생")
  @Test
  void createReadStatusWhenAlreadyExists() {
    basicReadStatusService.create(readStatusCreateRequest);
    assertThatThrownBy(() -> basicReadStatusService.create(readStatusCreateRequest))
        .isInstanceOf(IllegalArgumentException.class);
  }
}