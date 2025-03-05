package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;

// 생각해보니 User는 따로 레파지토리 안 만들어도 될 듯? 우선 둬보긴하자
public interface UserRepository {
    User findByName(String userName);

    List<User> findAll();

    List<User> findUpdatedUsers();

    void createUser(String userName, String nickName);

    void updateUser(String oldUserName, String newUserName, String newNickName);

    void deleteUser(String userName);
}
