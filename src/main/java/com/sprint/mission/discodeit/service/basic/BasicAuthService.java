package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserLoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserDto login(UserLoginDto loginRequestUser) {
        User user = findUserByNameAndValidatePassword(loginRequestUser);

        user.changeUserStatus();
        userRepository.save(user);
        userStatusRepository.save(user.getUserStatus());

        return UserDto.fromEntity(user);
    }

    private User findUserByNameAndValidatePassword(UserLoginDto loginRequestUser) {
        User user = validateUserName(loginRequestUser);
        validatePassword(user, loginRequestUser.password());

        return user;
    }

    private User validateUserName(UserLoginDto loginRequestUser) {
        return userRepository.findByName(loginRequestUser.username())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 등록되어 있지 않습니다. 회원가입부터 해주세요"));
    }

    private void validatePassword(User user, String password) {
        if (!user.isSamePassword(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
