package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.auth.LoginResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidCredentialsException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {       // findByUserName 에서 NullPointerException 과 NoSuchElementException 를 던질 수 있음
            User loginUser = userRepository.findByUserName(loginRequest.username());
            if (!loginUser.getPassword().equals(loginRequest.password())) {
                throw new InvalidCredentialsException();
            }
            return new LoginResponse(loginUser.getId(), loginUser.getUserName(), loginUser.getUserEmail(), loginUser.getProfileId());
        } catch (NullPointerException | NoSuchElementException e) {
            throw new InvalidCredentialsException();
        }
    }
}
