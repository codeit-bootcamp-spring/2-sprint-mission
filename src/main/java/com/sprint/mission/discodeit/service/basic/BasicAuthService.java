package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.User.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User with username " + username + " not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
        return user;
    }
}
