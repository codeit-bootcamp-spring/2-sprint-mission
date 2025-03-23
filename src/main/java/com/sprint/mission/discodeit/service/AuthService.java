package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User login(UserCreateDto createDto) {
        User user = userRepository.findByEmail(createDto.email());

        if (!createDto.password().equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return user;
    }
}
