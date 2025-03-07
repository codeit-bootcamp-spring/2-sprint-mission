package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class BasicUserService implements UserService {

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User getUser(String name) {
        return null;
    }

    @Override
    public List<User> getAllUser() {
        return List.of();
    }

    @Override
    public User update(String name, String changeName, String changeEmail) {
        return null;
    }

    @Override
    public void delete(String name) {

    }
}
