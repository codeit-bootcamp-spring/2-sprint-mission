package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.auth.LoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .filter(u -> u.getPassword().equals(request.password()))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 사용자명 또는 비밀번호입니다."));

        Optional<UserStatus> userStatuses = userStatusRepository.findByUserId(user.getId());
        boolean isOnline = userStatuses.stream().findFirst().map(status -> {
            status.updateStatus();
            userStatusRepository.save(status);
            return status.isOnline();
        }).orElse(false);

        return new LoginResponse(user.getId(), user.getUsername(), user.getEmail(), isOnline);
    }
}
