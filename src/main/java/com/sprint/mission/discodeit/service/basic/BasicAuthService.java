package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
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
  public User register(CreateUserRequest request) {
    String hashedPassword = BCrypt.hashpw(request.password(), BCrypt.gensalt());

    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("이미 존재하는 Email입니다");
    }
    if (userRepository.existsByUsername(request.username())) {
      throw new IllegalArgumentException("이미 존재하는 Username입니다");
    }

    User user = User.builder()
        .username(request.username())
        .email(request.email())
        .password(hashedPassword)
        .build();

    userRepository.addUser(user);
    UserStatus userStatus = UserStatus.builder()
        .userid(user.getId())
        .lastActiveAt(Instant.now())
        .build();

    userStatusRepository.addUserStatus(userStatus);

    return user;
  }

  @Override
  public User login(LoginRequest request) {
    User user = userRepository.findUserByName(request.username())
        .orElseThrow(NoSuchElementException::new);
    if (user == null) {
      throw new NoSuchElementException("존재하지 않는 유저입니다.");
    }

    // 비밀번호 검증
    if (!BCrypt.checkpw(request.password(), user.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    userStatusRepository.findUserStatusById(user.getId())
        .ifPresent(UserStatus::updateLastActiveAt);
    userStatusRepository.save();

    return user;
  }
}
