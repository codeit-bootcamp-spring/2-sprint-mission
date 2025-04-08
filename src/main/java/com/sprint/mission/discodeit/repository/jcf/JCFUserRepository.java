package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<User> loadToId(UUID id) {
        return userData.stream().filter(user -> user.getId().equals(id)).findFirst();
    }
}
