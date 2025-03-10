package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFUserRepository implements UserRepository {
    public final List<User> userData;

    public JCFUserRepository() {
        userData = new ArrayList<>();
    }


    @Override
    public void save(User user) {
        userData.add(user);
    }

    @Override
    public List<User> load() {
        return userData.stream().toList();
    }

    @Override
    public void deleteFromFile(User user) {
        userData.remove(user);
    }
}
