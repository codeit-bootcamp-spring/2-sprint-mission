package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.user.response.UserLoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(userLoginRequest.username()) &&
                                u.getPassword().equals(userLoginRequest.password()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Invalid username or password"));

        return new UserLoginResponse(user.getUsername(), user.getPassword(), user.getEmail());
    }
}