package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.AuthRequestDTO;
import com.sprint.mission.discodeit.DTO.AuthResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public AuthResponseDTO login(AuthRequestDTO authRequest) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getUserName().equals(authRequest.username()) && u.getEmail().equals(authRequest.password()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        return new AuthResponseDTO(user.getId(), user.getUserName());
    }
}
