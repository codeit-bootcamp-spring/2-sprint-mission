package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.application.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.sprint.mission.discodeit.constant.SetUpUserInfo.LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserStatusServiceTest {
    @Test
    void create() {
        UserRepository userRepository = new JCFUserRepository();
        User user = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        UserStatusService userStatusService = new BasicUserStatusService(userStatusRepository, userRepository);

        UserStatusDto userStatusDto = userStatusService.create(user.getId());
        assertThat(userStatusDto.id()).isNotNull();
    }

    @Test
    void create_NotUser() {
        UserRepository userRepository = new JCFUserRepository();
        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        UUID random = UUID.randomUUID();
        userStatusRepository.save(new UserStatus(random));
        UserStatusService userStatusService = new BasicUserStatusService(userStatusRepository, userRepository);


        assertThatThrownBy(() -> userStatusService.create(random)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_AlreadyUserStatusExist() {
        UserRepository userRepository = new JCFUserRepository();
        User user = userRepository.save(new User(LOGIN_USER.getName(), LOGIN_USER.getEmail(), LOGIN_USER.getPassword(), null));

        UserStatusRepository userStatusRepository = new JCFUserStatusRepository();
        UserStatusService userStatusService = new BasicUserStatusService(userStatusRepository, userRepository);

        userStatusService.create(user.getId());

        assertThatThrownBy(() -> userStatusService.create(user.getId())).isInstanceOf(IllegalArgumentException.class);
    }

}