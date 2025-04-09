package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserResponseDto login(LoginRequest request) {
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + request.username()));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(
            () -> new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + user.getId()));

    if (!request.password().equals(user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    userStatus.updateByUserId();
    userStatusRepository.save(userStatus);

    boolean online = checkUserOnlineStatus(user.getId());
    return UserResponseDto.convertToResponseDto(user, online);
  }
  
  private boolean checkUserOnlineStatus(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId));
    return userStatus.isOnline();
  }


}
