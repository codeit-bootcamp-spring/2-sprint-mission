package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.User.UserDto;
import com.sprint.mission.discodeit.entity.Status;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto login(String email, String password) {
        Optional<User> optionalUser = userRepository.findAll().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return UserDto.fromUser(user, new UserStatus(user.getId(), Status.ONLINE));
            }
        }
        throw new IllegalArgumentException("Invalid email or password.");
    }
}
