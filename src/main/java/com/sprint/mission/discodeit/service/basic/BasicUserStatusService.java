package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.SaveUserStatusParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public void save(SaveUserStatusParamDto saveUserStatusParamDto) {
    UserStatus userStatus = UserStatus.builder()
        .userUUID(saveUserStatusParamDto.userUUID())
        .build();
    userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus findById(UUID userStatusUUID) {
    return userStatusRepository.findById(userStatusUUID)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자 상태"));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public void updateByUserId(UUID userId, UpdateUserStatusRequestDto updateUserStatusRequestDto) {
    User user = userRepository.findUserById(userId)
        .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new NoSuchElementException("사용자 상태 수정 오류"));
    userStatus.updateLastLoginTime(updateUserStatusRequestDto.loginTime());
    userStatusRepository.save(userStatus);
  }


  @Override
  public void delete(UUID userStatusUUID) {
    userStatusRepository.delete(userStatusUUID);
  }
}
