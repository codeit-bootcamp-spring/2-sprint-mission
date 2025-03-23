package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.readstatusdto.ReadStatusDto;
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
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ChannelInfo.CHANNEL_NAME;
import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReadStatusServiceTest {
    @Test
    void create() {
        UserRepository userRepository = new JCFUserRepository();
        User user = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        ChannelRepository channelRepository = new JCFChannelRepository();
        Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, CHANNEL_NAME));

        ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();
        BasicReadStatusService basicReadStatusService = new BasicReadStatusService(readStatusRepository, channelRepository, userRepository);
        ReadStatusDto readStatus = basicReadStatusService.create(user.getId(), channel.getId());

        assertThat(readStatus.id()).isNotNull();
    }

    @Test
    void create_NonUser() {
        UserRepository userRepository = new JCFUserRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();
        Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, CHANNEL_NAME));

        ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();
        BasicReadStatusService basicReadStatusService = new BasicReadStatusService(readStatusRepository, channelRepository, userRepository);

        assertThatThrownBy(() -> basicReadStatusService.create(UUID.randomUUID(), channel.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_NoChannel() {
        UserRepository userRepository = new JCFUserRepository();
        User user = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        ChannelRepository channelRepository = new JCFChannelRepository();
        ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();
        BasicReadStatusService basicReadStatusService = new BasicReadStatusService(readStatusRepository, channelRepository, userRepository);

        assertThatThrownBy(() -> basicReadStatusService.create(user.getId(), UUID.randomUUID())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_readStatusAlready() {
        UserRepository userRepository = new JCFUserRepository();
        User user = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        ChannelRepository channelRepository = new JCFChannelRepository();
        Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, CHANNEL_NAME));

        ReadStatusRepository readStatusRepository = new JCFReadStatusRepository();
        BasicReadStatusService basicReadStatusService = new BasicReadStatusService(readStatusRepository, channelRepository, userRepository);
        basicReadStatusService.create(user.getId(), channel.getId());

        assertThatThrownBy(() -> basicReadStatusService.create(user.getId(), channel.getId())).isInstanceOf(IllegalArgumentException.class);
    }
}