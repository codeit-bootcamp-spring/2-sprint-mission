package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(String userId, String username, String userPwd, String userEmail, String userPhone);
    User read(String userId);
    String getUserName(UUID uuid);
    String getUserId(UUID uuid);
    List<User> readAll(List<String> userIdList);
    UUID login(String userId, String userPwd);

    User update(UUID loginUserUuid, String userId, String userPwd, String userEmail, String userPhone);
    void delete(UUID loginUserUuid);
}
