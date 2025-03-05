package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UUID create(String userId, String username, String userPwd, String userEmail, String userPhone);
    User read(String userId);
    String getUserName(UUID userKey);
    String getUserId(UUID userKey);
    List<User> readAll(List<String> userIdList);
    UUID login(String userId, String userPwd, UUID loginUserKey);
    void logOut(UUID userKey);
    UUID update(UUID userKey, String userId, String userPwd, String userEmail, String userPhone);
    void delete(UUID userKey);
}
