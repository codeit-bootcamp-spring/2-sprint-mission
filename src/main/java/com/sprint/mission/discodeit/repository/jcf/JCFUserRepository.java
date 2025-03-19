package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFUserRepository implements UserRepository {
    private final List<User> userData;

    public JCFUserRepository() {
        userData = new ArrayList<>();
    }


    @Override
    public User save(User user) {
        userData.add(user);
        return user;
    }

    @Override
    public List<User> load() {
        return userData.stream().toList();
    }

    @Override
    public void remove(User user) {
        userData.remove(user);
    }
}
