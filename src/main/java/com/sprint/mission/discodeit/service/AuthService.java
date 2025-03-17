package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        return userOptional
                .filter(user -> user.getPassword().equals(request.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }
}