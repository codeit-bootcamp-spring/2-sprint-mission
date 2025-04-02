package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.auth.request.UserLoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(UserLoginDto loginDto) {
        return userRepository.findByUserName(loginDto.userName())
                .map(user -> {
                    if (!passwordEncoder.matches(loginDto.userPassword(), user.getPassword())) {
                        throw new RuntimeException("아이디 또는 비밀번호가 잘못되었습니다.");
                    }
                    return user;
                })
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 잘못되었습니다."));

    }
}
