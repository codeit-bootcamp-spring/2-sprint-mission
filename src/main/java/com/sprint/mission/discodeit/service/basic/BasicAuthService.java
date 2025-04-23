package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.login.LoginForm;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto login(LoginForm form) {

        String username = form.username();
        String password = form.password();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("[ERROR] user not found");
        }
        boolean result = user.getPassword().equals(password);
        if (!result) {
            throw new IllegalArgumentException("[ERROR] try again");
        }

        return userMapper.toDto(user);
    }
}
