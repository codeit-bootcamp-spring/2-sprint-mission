package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.AuthLoginDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.InvalidPasswordException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto login(AuthLoginDto authLoginDto) {
        String username = authLoginDto.username();
        String password = authLoginDto.password();

        User foundUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!foundUser.getPassword().equals(password)) {
            throw new InvalidPasswordException(password);
        }

        foundUser.getStatus().update(Instant.now());

        return userMapper.toDto(foundUser);
    }
}
