package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.AuthException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.jwt.JwtUtil;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final JwtUtil jwtUtil;


  @Override
  public String login(UserDto.Login loginDto) {
    // 이메일과 비밀번호 검증
    User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(
        () -> new ResourceNotFoundException("User", loginDto.getEmail(), "존재하지 않는 이메일입니다."));

    if (!user.checkPassword(loginDto.getPassword())) {
      throw new AuthException("이메일 또는 비밀번호가 일치하지 않습니다.");
    }
    userStatusService.setUserOnline(user.getId());
    return jwtUtil.generateToken(String.valueOf(user.getId()));
  }
}