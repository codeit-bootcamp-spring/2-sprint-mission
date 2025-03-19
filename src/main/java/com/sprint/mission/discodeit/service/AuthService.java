package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(@Qualifier("fileUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    List<User> login() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new IllegalArgumentException("user 객체가 없습니다.");
        }
        List<User> filteredUsers = users.stream().filter(user -> user.getUsername().equals(user.getPassword()))
                .toList();
        if (filteredUsers.isEmpty()) {
            throw new NoSuchElementException("username과 password가 일치하는 유저는 없습니다.");
        }
        return filteredUsers;
    }
}
