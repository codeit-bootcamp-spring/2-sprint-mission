package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.login.LoginForm;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(LoginForm form) {

        String username = form.getUsername();
        String password = form.getPassword();

        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new NoSuchElementException("[ERROR] user not found");
        }
        boolean result = user.getPassword().equals(password);
        if (!result) {
            throw new IllegalArgumentException("[ERROR] try again");
        }
        user.updateLastLoginAt(Instant.now());

        return user;
    }
}
