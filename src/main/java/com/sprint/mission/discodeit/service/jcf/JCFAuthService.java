package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.login.LoginForm;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class JCFAuthService implements AuthService {

    UserRepository userRepository;

    @Override
    public User login(LoginForm form) {

        User user = userRepository.findByUserName(form.getName());
        boolean result = user.getPassword().equals(form.getPassword());
        if (!result) {
            throw new IllegalArgumentException("[ERROR] 다시 입력 해주세요.");
        }

        user.updateLastLoginAt(Instant.now());

        return user;
    }
}
