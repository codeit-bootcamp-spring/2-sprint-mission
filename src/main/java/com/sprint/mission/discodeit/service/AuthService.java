package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.UserService.AuthDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User login(AuthDTO authDTO) {
        return userRepository.findAll().stream().
                filter(u -> u.getUserName().equals(authDTO.userName())).
                filter(u -> u.getPassword().equals(authDTO.password())).
                findFirst().orElseThrow();
    }

}
