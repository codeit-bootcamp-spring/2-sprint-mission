package com.sprint.mission.discodeit.core.status.usecase.user;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.port.UserStatusRepositoryPort;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.CreateUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UpdateUserStatusCommand;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import com.sprint.mission.discodeit.exception.AlreadyExistsException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NormalException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserRepositoryPort userRepository;
  private final UserStatusRepositoryPort userStatusRepository;

  /**
   * <h2>유저 상태 생성 메서드</h2>
   * 유저 상태를 생성하는 메서드 <br> 단독으로 쓸 일이 없기에 최소한의 검증만 진행
   *
   * @param command 유저 아이디, 시각 정보
   * @return 유저 상태
   */
  @Transactional
  @Override
  public UserStatus create(CreateUserStatusCommand command) {
    User user = command.user();
    //유저 상태가 이미 존재하면 오류 발생
    if (userStatusRepository.findByUserId(user.getId()).isPresent()) {
      throw new AlreadyExistsException(ErrorCode.USER_STATUS_ALREADY_EXISTS,
          user.getUserStatus().getId());
    }

    //유저 상태가 존재하지 않으면 진행
    UserStatus userStatus = UserStatus.create(user, command.lastActiveAt());
    userStatusRepository.save(userStatus);
    return userStatus;
  }

/*
 한번도 사용한 적이 없는 코드들
  @Override
  @Transactional(readOnly = true)
  public UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> userStatusIdNotFoundError(userId));
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatus findByStatusId(UUID userStatusId) {
    return userStatusRepository.findByStatusId(userStatusId)
        .orElseThrow(() -> userStatusIdNotFoundError(userStatusId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }
*/

  /**
   * <h2>유저 상태 업데이트 메서드</h2>
   * 유저의 상태를 업데이트함
   *
   * @param command 유저 id, 유저 상태 id, 날짜정보
   * @return 유저 상태
   */
  @Override
  @Transactional
  public UserStatus update(UpdateUserStatusCommand command) {
    UserStatus userStatus;
    //dto에 유저 아이디가 존재하면 repo에서 유저 아이디를 바탕으로 유저 상태를 검색
    if (command.userId() != null) {
      userStatus = userStatusRepository.findByUserId(command.userId()).orElseThrow(
          () -> new NotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, command.userId())
      );
      //dto에 유저 상태 아이디가 존재하면 repo에서 유저 상태를 바탕으로 유저 상태를 검색
    } else if (command.userStatusId() != null) {
      userStatus = userStatusRepository.findByStatusId(command.userStatusId()).orElseThrow(
          () -> new NotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, command.userStatusId())
      );
    } else {
      throw new NormalException("Update Error");
    }
    //업데이트 진행
    userStatus.update(command.newLastActiveAt());
    return userStatus;
  }

  /**
   * <h2>유저 상태 온라인 확인 메서드</h2>
   * 유저 상태가 온라인인지 아닌 지 확인
   *
   * @param userId
   * @return 현재시각 기준, 유저가 로그인한 지 5분 이내이다 = true, 5분 이후이다 = false
   */
  @Override
  public boolean isOnline(UUID userId) {
    UserStatus status = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> new NotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, userId)
    );
    return status.isOnline();
  }

  /**
   * <h2>유저 상태 제거 메서드</h2>
   * 유저 상태를 제거하는 메서드 <br> 단독으로 쓰일 일 거의 없음
   *
   * @param userId 삭제할 아이디
   */
  @Override
  @Transactional
  public void delete(UUID userId) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> new NotFoundException(ErrorCode.USER_STATUS_NOT_FOUND, userId)
    );

    userStatusRepository.delete(userStatus.getId());
  }

}
