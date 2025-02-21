package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Set;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService() {
        this.userRepository = new UserRepository();
    }

    public void createUser(User newUser) {
        this.userRepository.addUser(newUser);
    }

    public User readUser(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("null 인 id 값이 들어왔습니다!!!");
        }
        return userRepository.findUserById(id);
    }

    public Set<User> readAllUsers() {
        return userRepository.getUsers();
    }

    public void updateUserName(UUID id, String newUserName) {
        readUser(id).updateUserName(newUserName);
    }

    public void updatePassword(UUID id, String newPassword) {
        readUser(id).updateUserPassword(newPassword);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteUser(id);
    }
}
