package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;


    @Override
    public User create(String username, String email, String password) {
        User user = new User(username,email,password);
        return userRepository.save(user);
    }

    @Override
    public User find(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        return userRepository.update(userId,newUsername,newEmail,newPassword);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.delete(userId);
    }
}
