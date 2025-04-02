package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public User login(LoginRequest request) {
        return userRepository.getAllUsers().stream()
                .filter(user -> user.getEmail().equals(request.email()))
                .filter(user -> user.getPassword().equals(request.password()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));
    }
}
