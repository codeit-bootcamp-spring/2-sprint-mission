package com.sprint.mission.discodeit.auth.service.basic;

import com.sprint.mission.discodeit.auth.dto.LoginRequest;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Transactional
    @Override
    public UserResult login(LoginRequest loginRequestUser) {
        User user = findUserByNameAndValidatePassword(loginRequestUser);
        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저의 상태가 존재하지 않습니다."));

        Instant now = Instant.now();
        userStatus.updateLastActiveAt(now);

        return UserResult.fromEntity(user, userStatus.isOnline(now));
    }

    private User findUserByNameAndValidatePassword(LoginRequest loginRequestUser) {
        User user = validateUserName(loginRequestUser);
        validatePassword(user, loginRequestUser.password());

        return user;
    }

    private User validateUserName(LoginRequest loginRequestUser) {
        return userRepository.findByName(loginRequestUser.username())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저는 등록되어 있지 않습니다. 회원가입부터 해주세요"));
    }

    private void validatePassword(User user, String password) {
        if (!user.isSamePassword(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
