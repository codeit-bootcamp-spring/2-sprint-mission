package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    // CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능
    User createUser(String userName);
    void getUserInfo(String userName);
    void getAllUserData();
    void updateUserName(String oldUserName, String newUserName);
    void deleteUserName(String userName);
    UUID findUserId(String userName);
}
