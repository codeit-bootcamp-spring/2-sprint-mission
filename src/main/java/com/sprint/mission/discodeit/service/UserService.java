package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    //어떤 기능이 있는지 추상적으로
    //생성
    void createUser(String username, Channel channel);
    User findByUserId(UUID userId);
    UUID findByUsername(String username);
}
