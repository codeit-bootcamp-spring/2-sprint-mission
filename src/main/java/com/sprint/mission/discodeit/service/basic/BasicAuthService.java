package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.AuthException;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.dto.request.authdto.AuthServiceLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserJPARepository userJpaRepository;

    @Override
    @Transactional
    public User login(AuthServiceLoginDto authServiceLoginDto) {
        User matchingUserUser = userJpaRepository.findByUsername(authServiceLoginDto.username())
                .orElseThrow(() -> new AuthException("User not found"));
        if (!matchingUserUser.getPassword().equals(authServiceLoginDto.password())) {
            throw new AuthException("Password does not match");
        }
        return matchingUserUser;
    }
}
