package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.AuthResDto;
import com.sprint.mission.discodeit.dto.auth.LoginResDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public AuthResDto login(LoginResDto loginResDto) throws Exception {
        // username, password와 일치하는 유저 조회
        User user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(loginResDto.username()) &&
                        u.getPassword().equals(loginResDto.password()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User with id " + loginResDto.username() + " " + loginResDto.password() + " not found"));

        return new AuthResDto(user.getId(), user.getUsername(), user.getEmail(), user.getProfileId());
    }
}
