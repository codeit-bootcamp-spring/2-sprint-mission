package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;

public interface UserService {
    User getUser(String userName);

    List<User> getAllUsers();

    List<User> getUpdatedUsers();

    void registerUser(String userName, String nickName);

    void updateName(String oldUserName, String newUserName, String newNickName);

    void deleteUser(String userName);
}
