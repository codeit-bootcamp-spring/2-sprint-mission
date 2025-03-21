package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface UserStatusService {

    void create(String userId);

    UserStatus findByUserId(String userId);

    UserStatus findByStatusId(String userStatusId);

    List<UserStatus> findAll();

//    void update(String userId, String replaceId);

    void delete(String userStatusId);
}
