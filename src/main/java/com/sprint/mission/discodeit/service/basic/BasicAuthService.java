package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthLoginDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.custom.auth.InvalidPasswordException;
import com.sprint.mission.discodeit.exception.custom.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusService userStatusService;

    @Override
    public User login(AuthLoginDto authLoginDto) {
        String username = authLoginDto.username();
        String password = authLoginDto.password();

        User foundUser = userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (foundUser == null) {
            throw new UserNotFoundException("로그인 실패: 유저를 찾을 수 없습니다.");
        }

        if (!foundUser.getPassword().equals(password)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        UserStatusUpdateByUserIdDto userStatusUpdateByUserIdDto = new UserStatusUpdateByUserIdDto(Instant.now());
        userStatusService.updateByUserId(foundUser.getId(), userStatusUpdateByUserIdDto);

        return foundUser;
    }
}
