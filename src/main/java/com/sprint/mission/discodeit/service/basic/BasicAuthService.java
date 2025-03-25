package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequest;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponse;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;

    @Override
    public AuthLoginResponse login(AuthLoginRequest requestDto) {
        User user = userRepository.findByUsername(requestDto.username())
                .filter(u -> u.getPassword().equals(requestDto.password()))
                .orElseThrow(() -> new NoSuchElementException("로그인 실패"));

        userStatusService.updateByUserId(user.getId());

        return new AuthLoginResponse(user.getId(), user.getEmail(), user.getUsername());
    }
}
