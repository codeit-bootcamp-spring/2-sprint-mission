package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserService.AuthRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    public User login(AuthRequest authRequest) {
        return userRepository.findAll().stream().
                filter(u -> u.getUserName().equals(authRequest.userName())).
                filter(u -> u.getPassword().equals(authRequest.password())).
                findFirst().orElseThrow();
    }

}
