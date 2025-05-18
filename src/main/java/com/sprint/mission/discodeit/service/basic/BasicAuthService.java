package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.warn("로그인 실패");
                return new UserNotFoundException(username);
            });

        if (!user.getPassword().equals(password)) {
            log.warn("로그인 실패");
            throw new IllegalArgumentException("Wrong password");
        }

        log.info("로그인 성공");
        return userMapper.toDto(user);
    }
}
