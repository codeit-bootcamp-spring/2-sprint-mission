package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public RegisterResponse register(CreateUserRequest request) {
        User user = userService.createUser(request);

        return RegisterResponse.builder()
                .success(true)
                .message("회원 가입이 완료되었습니다.")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findUserByName(request.getUsername());
        if (user == null) {
            throw new NoSuchElementException("존재하지 않는 유저입니다.");
        }

        // 비밀번호 검증
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getId().toString());

        return LoginResponse.builder()
                .success(true)
                .message("성공적으로 로그인 되었습니다.")
                .token(token)
                .build();
    }

    private UserInfoDto mapToUserFindDto(User user) {
        UserInfoDto dto = new UserInfoDto();
        dto.setUserid(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setStatus(UserStatusType.Online);
        return dto;
    }
}
