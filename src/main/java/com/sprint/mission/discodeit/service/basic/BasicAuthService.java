package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.LoginDto;
import com.sprint.mission.discodeit.service.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponseDto login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + loginDto.email()));
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + user.getId()));

        if (!loginDto.password().equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        userStatus.updateByUserId();
        userStatusRepository.save(userStatus);

        boolean isOnline = checkUserOnlineStatus(user.getId());
        return UserResponseDto.convertToResponseDto(user, isOnline);
    }

    private boolean checkUserOnlineStatus(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId));
        return userStatus.isOnline();
    }


}
