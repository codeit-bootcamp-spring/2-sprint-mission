package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFUserRepository implements UserRepository {

    private final List<User> userDataList = new ArrayList<>();

    @Override
    public User userSave(String nickname, String password) {
        User user = new User(nickname, password);
        userDataList.add(user);
        return user;
    }
}
