package com.sprint.mission.discodeit.domain.auth.service;

import com.sprint.mission.discodeit.domain.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.domain.auth.exception.AuthPasswordNotMatchException;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.domain.userstatus.exception.UserStatusNotFoundException;
import com.sprint.mission.discodeit.domain.userstatus.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Transactional
    @Override
    public UserResult login(LoginRequest loginRequest) {
        User user = validateUserName(loginRequest);
        validatePassword(user, loginRequest.password());
        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", user.getId())));

        Instant now = Instant.now();
        userStatus.updateLastActiveAt(now);
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        return UserResult.fromEntity(user, savedUserStatus.isOnline(now));
    }

    private User validateUserName(LoginRequest loginRequestUser) {
        return userRepository.findByName(loginRequestUser.username())
                .orElseThrow(() -> new UserNotFoundException(Map.of("userName", loginRequestUser.username())));
    }

    private void validatePassword(User user, String password) {
        if (!user.isSamePassword(password)) {
            throw new AuthPasswordNotMatchException(Map.of("userId", user.getId()));
        }
    }

}
