package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AuthenticationManager authenticationManager;

  @Transactional(readOnly = true)
  @Override
  public UserDto login(LoginRequest loginRequest, HttpServletRequest request) {
    log.debug("로그인 시도: username={}", loginRequest.username());
    
    String username = loginRequest.username();
    String password = loginRequest.password();

    Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
    );
    SecurityContextHolder.getContext().setAuthentication(auth);

    System.out.println("현재 인증 정보: " + auth);

    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> UserNotFoundException.withUsername(username));

    return userMapper.toDto(user);
  }
}
