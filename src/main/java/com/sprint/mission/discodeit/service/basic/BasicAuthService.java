package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public UserDto login(LoginRequest dto) {
        String username = dto.username();
        String password = dto.password();

        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new NoSuchElementException("User with username " + username + " not found"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Wrong password");
        }
        return userMapper.toDto(user);
    }
}
