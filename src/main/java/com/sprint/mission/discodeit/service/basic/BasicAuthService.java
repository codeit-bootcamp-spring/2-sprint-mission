package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthLoginDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto login(AuthLoginDto authLoginDto) {
        User foundUser = userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(authLoginDto.username()) && user.getPassword()
                        .equals(authLoginDto.password())).findFirst()
                .orElse(null);

        if (foundUser == null) {
            throw new NoSuchElementException("로그인 실패: 유저를 찾을 수 없습니다.");
        }

        return new UserResponseDto(foundUser, true); // 로그인 했으니까 온라인 상태 true
    }
}
