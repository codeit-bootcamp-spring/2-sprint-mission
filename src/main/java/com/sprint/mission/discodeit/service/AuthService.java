package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.user.LoginRequest;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new NoSuchElementException(loginRequest.username() + " 와 동일한 username이 없음"));
        if (!user.getPassword().equals(loginRequest.password())) {
            throw new IllegalArgumentException("login 실패");
        }
        return user;
    }
}
