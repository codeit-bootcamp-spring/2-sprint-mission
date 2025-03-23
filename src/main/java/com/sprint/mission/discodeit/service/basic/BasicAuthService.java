package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequestDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public LoginRequestDTO login(LoginRequestDTO loginRequestDTO) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequestDTO.getUsername());
        User user = optionalUser
                .filter(u -> u.getPassword().equals(loginRequestDTO.getPassword()))
                .orElseThrow(() -> new NoSuchElementException("Invalid username or password"));

        return new LoginRequestDTO(user.getUsername(), user.getPassword());
    }
}
