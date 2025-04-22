package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
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

    // 메서드 몇칸 띌건지
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
