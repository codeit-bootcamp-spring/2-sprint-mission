package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserStatusService {
    void create(String userId);
    UserStatus find(String userId);
    List<UserStatus> findAll();
    void update(String userId, String replaceId);
    void delete(String userId);
}
