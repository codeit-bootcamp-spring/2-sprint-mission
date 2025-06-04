package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.Auth.AuthFailException;
import com.sprint.mission.discodeit.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.request.authdto.AuthServiceLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserJPARepository userJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public User login(AuthServiceLoginDto authServiceLoginDto) {
        User matchingUserUser = userJpaRepository.findByUsername(authServiceLoginDto.username())
                .orElseThrow(() -> new UserNotFoundException(Map.of("userName", authServiceLoginDto.username() )));
        if (!matchingUserUser.getPassword().equals(authServiceLoginDto.password())) {
            throw new AuthFailException(Map.of("userId", matchingUserUser.getId()));
        }
        return matchingUserUser;
    }
}
