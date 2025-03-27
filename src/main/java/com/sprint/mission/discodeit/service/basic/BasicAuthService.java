package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.auth.AuthResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public AuthResponseDto login(AuthLoginRequestDto dto) {
        Optional<User> matchingUser = userRepository.findAll()
                .stream()
                .filter(user -> Objects.equals(user.getUsername(), dto.getUsername())
                        && Objects.equals(user.getPassword(), dto.getPassword()))
                .findFirst();

        return matchingUser.map(user -> new AuthResponseDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(), user.getEmail(), user.getProfileId()))
                .orElseThrow(() -> new NoSuchElementException("User with name " + dto.getUsername() + " not found"));
    }
}
