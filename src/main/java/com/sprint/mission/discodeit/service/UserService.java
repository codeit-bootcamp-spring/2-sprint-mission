package com.sprint.mission.discodeit.service;

public interface UserService {
    String getUser(String userName);
    String getAllUsers();
    void registerUser(String userName, String nickName);
}
