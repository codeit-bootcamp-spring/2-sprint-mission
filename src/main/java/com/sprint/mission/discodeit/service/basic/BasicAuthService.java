package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.NoSuchElementException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()->new NoSuchElementException("User not found with username:" + loginRequest.getUsername()));

        if(!user.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElse(new UserStatus(user.getId(), Instant.now()));
        userStatus.updatedLastActiveAt(Instant.now());
        userStatusRepository.save(userStatus);

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatus.isUserOnline()
        );
    }
}
