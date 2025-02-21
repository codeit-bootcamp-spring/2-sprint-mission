package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;

public interface UserService {
    String getUser(String userName);
    String getAllUsers();
    List<User> getUpdatedUsers();
    void registerUser(String userName, String nickName);
    boolean userNameExists(String userName);
    void updateName(String oldUserName, String userName, String newName);
    String deleteUser(String userName);
}
