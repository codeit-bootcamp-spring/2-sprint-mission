package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public void login(LoginRequestDto loginRequestDto) {
        User userInfo = userRepository.findUserByUsername(loginRequestDto.username())
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 일치하지 않음"));

        if (!userInfo.getPassword().equals(loginRequestDto.password())) {
            throw new IllegalArgumentException("회원정보가 일치하지 않음");
        }

        UserStatus userStatus = userStatusRepository.findByUserId(userInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("회원상태 업데이트 에러"));

        userStatusRepository.update(userStatus.getId());
    }
}
