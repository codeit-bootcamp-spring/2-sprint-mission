package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.AuthException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(AuthServiceLoginRequest authServiceLoginRequest) {
        List<User> UserList = userRepository.load();
        User matchingUserUser = UserList.stream().filter(m -> m.getUsername().equals(authServiceLoginRequest.username())).findAny()
                .orElseThrow(() -> new AuthException("User not found"));

        if (!matchingUserUser.getPassword().equals(authServiceLoginRequest.password())) {
            throw new AuthException("Password does not match");
        }

        return matchingUserUser;
    }
}
