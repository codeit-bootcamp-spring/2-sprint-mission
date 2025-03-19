package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.userDto.AuthLoginRequest;
import com.sprint.mission.discodeit.service.userDto.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse login(AuthLoginRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // 비밀번호 일치 여부 확인 (실제 구현에서는 비밀번호 해싱 로직 필요!)
        if (!user.getPassword().equals(request.password())) {
            throw new RuntimeException("Invalid username or password");
        }

        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
