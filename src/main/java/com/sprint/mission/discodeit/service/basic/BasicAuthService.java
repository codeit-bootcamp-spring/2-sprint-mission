package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.AuthException;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginRequest;
import com.sprint.mission.discodeit.service.dto.authdto.AuthServiceLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public AuthServiceLoginResponse login(AuthServiceLoginRequest authServiceLoginRequest) {
        List<User> UserList = userRepository.load();
        User matchingUserUser = UserList.stream().filter(m -> m.getName().equals(authServiceLoginRequest.username())).findAny()
                .orElseThrow(() -> new AuthException("User not found"));

        if (!matchingUserUser.getPassword().equals(authServiceLoginRequest.password())) {
            throw new AuthException("Password does not match");
        }

        return AuthServiceLoginResponse.authLogin(true, matchingUserUser);
    }
}
