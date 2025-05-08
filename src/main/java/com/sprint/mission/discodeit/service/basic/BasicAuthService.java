package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.auth.LoginResponse;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserWrongPasswordException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
            .orElseThrow(() -> UserNotFoundException.forUsername(loginRequest.username()));

        if (!user.getPassword().equals(loginRequest.password())) {
            throw new UserWrongPasswordException();
        }

        return userMapper.toDto(user);
    }
}
