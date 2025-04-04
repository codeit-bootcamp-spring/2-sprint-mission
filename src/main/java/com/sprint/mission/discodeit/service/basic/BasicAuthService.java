package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserRequest;
import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.LoginResponse;
import com.sprint.mission.discodeit.dto.RegisterResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final JwtUtil jwtUtil;

  @Override
  public RegisterResponse register(CreateUserRequest request) {
    String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("이미 존재하는 Email입니다");
    }
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new IllegalArgumentException("이미 존재하는 Username입니다");
    }

    User user = new User(request.getUsername(), request.getEmail(), hashedPassword);
    userRepository.addUser(user);
    userStatusRepository.addUserStatus(new UserStatus(user.getId(), Instant.now()));

    return RegisterResponse.builder()
        .userId(user.getId())
        .success(true)
        .message("회원 가입이 완료되었습니다.")
        .build();
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    User user = userRepository.findUserByName(request.getUsername())
        .orElseThrow(NoSuchElementException::new);
    if (user == null) {
      throw new NoSuchElementException("존재하지 않는 유저입니다.");
    }

    // 비밀번호 검증
    if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    userStatusRepository.findUserStatusById(user.getId())
        .ifPresent(status -> status.updateLastActiveAt());
    userStatusRepository.save();

    String token = jwtUtil.generateToken(user.getId().toString());

    return LoginResponse.builder()
        .success(true)
        .message("성공적으로 로그인 되었습니다.")
        .token(token)
        .build();
  }
}
