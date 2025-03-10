package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;

public interface UserRepository {
    boolean userExists(String userName);

    User findByName(String userName);

    List<User> findAll();

    List<User> findUpdatedUsers();

    void createUser(String userName, String nickName);

    void updateUser(String oldUserName, String newUserName, String newNickName);

    void deleteUser(String userName);
}
