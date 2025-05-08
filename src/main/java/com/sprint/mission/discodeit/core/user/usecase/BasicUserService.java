package com.sprint.mission.discodeit.core.user.usecase;


import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.status.usecase.user.UserStatusService;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.CreateUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.OnlineUserStatusCommand;
import com.sprint.mission.discodeit.core.status.usecase.user.dto.UserStatusResult;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.core.user.exception.UserLoginFailedException;
import com.sprint.mission.discodeit.core.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.LoginUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final JpaUserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;


  /**
   * <h2>유저 생성 메서드</h2>
   * 유저를 생성한다. <br> 동일한 이름, 이메일이 존재하면 오류를 발생, <br> 내부에서 바이너리 데이터, 유저 상태를 생성한다.
   *
   * @param command          유저 이름, 이메일, 패스워드
   * @param binaryContentDTO 파일 이름, 파일 타입, 바이트
   * @return 아이디, 이름, 이메일, 프로필 이미지 메타데이터, 온라인 여부
   */
  @Override
  @Transactional
  public UserResult create(CreateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO) {
    BinaryContent profile = null;
    validateUser(command.username(), command.email());

    if (binaryContentDTO.isPresent()) {
      profile = binaryContentService.create(binaryContentDTO.orElse(null));
    }

    User user = User.create(command.username(), command.email(), command.password(), profile);
    userRepository.save(user);
    log.info("[UserService] User registered: id {}, name {}", user.getId(), user.getName());

    CreateUserStatusCommand statusCommand = new CreateUserStatusCommand(user, Instant.now());
    UserStatus userStatus = userStatusService.create(statusCommand);

    user.setUserStatus(userStatus);

    return UserResult.create(user, true);
  }

  @Override
  @Transactional
  public UserResult login(LoginUserCommand command) {
    User user = userRepository.findByName(command.username()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.username())
    );

    if (!command.password().equals(user.getPassword())) {
      throw new UserLoginFailedException(ErrorCode.LOGIN_FAILED, user.getId(), "Password mismatch");
    }

    //로그인한 시점에 유저 상태의 정보를 업데이트해야함
    user.getUserStatus().updateTime(Instant.now());

    log.info("[UserService] User login: id {}, username {}, password  {}", user.getId(),
        user.getName(),
        user.getPassword());

    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  /**
   * <h2>동일 이름, 이메일 확인 메서드</h2>
   * 동일한 이름과 이메일이 있으면 오류를 발생
   *
   * @param name  확인할 이름
   * @param email 확인할 이메일
   */
  private void validateUser(String name, String email) {
    if (userRepository.existsByName(name)) {
      throw new UserAlreadyExistsException(ErrorCode.USER_NAME_ALREADY_EXISTS, name);
    }

    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyExistsException(ErrorCode.USER_EMAIL_ALREADY_EXISTS, email);
    }
  }

  /**
   * <h2>유저 찾기 메서드</h2>
   * id를 통해서 유저를 찾음
   *
   * @param userId
   * @return 아이디, 이름, 이메일, 프로필 이미지 메타데이터, 온라인 여부
   */
  @Override
  @Transactional(readOnly = true)
  public UserResult findById(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));
    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  /**
   * <h2>유저 전체 찾기 메서드</h2>
   * 등록된 모든 유저를 출력함
   *
   * @return List [아이디, 이름, 이메일, 프로필 이미지 메타데이터, 온라인 여부]
   */
  @Override
  @Transactional(readOnly = true)
  public List<UserResult> findAll() {
    List<User> userList = userRepository.findAll();

    return userList.stream().map(user -> UserResult.create(
        user,
        user.getUserStatus().isOnline())
    ).toList();
  }

  /**
   * <h2>유저 업데이트 메서드</h2>
   * 유저를 업데이트하는 메서드   <br> 유저를 찾은 뒤, 기존 이미지가 존재하고, 또 업데이트할 이미지가 존재하면 기존 이미지를 삭제함 <br> 업데이트를 위해서
   * 트랜잭션을 걸었음
   *
   * @param command          바꿀 대상의 유저 아이디, 새 이름, 새 이메일, 새 패스워드
   * @param binaryContentDTO 바꿀 이미지
   * @return 아이디, 이름, 이메일, 프로필 이미지 메타데이터, 온라인 여부
   */
  @Override
  @Transactional
  public UserResult update(UpdateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO) {
    //유저를 찾음, 없으면 오류 발생
    User user = userRepository.findById(command.requestUserId())
        .orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.requestUserId()));

    //유저의 프로필 이미지를 가져옴
    BinaryContent profile = user.getProfile();

    //프로필이 있으면 새로 만들어야하기에 기존 이미지를 삭제 진행함
    //업데이트할 이미지가 없는데 기존 프로필이 존재하면 기존 프로필 이미지를 유지함
    if (profile != null && binaryContentDTO.isPresent()) {
      binaryContentService.delete(profile.getId());
    }
    BinaryContent newProfile = binaryContentService.create(binaryContentDTO.orElse(null));

    //유저 엔티티 내부의 업데이트 메서드를 진행함
    user.update(command.newName(), command.newEmail(), command.newPassword(), newProfile);
    log.info("[UserService] User Updated: username {}, email {}, password {}", user.getName(),
        user.getEmail(),
        user.getPassword());
    return UserResult.create(user, user.getUserStatus().isOnline());
  }

  /**
   * <h2>유저 제거 메서드</h2>
   * 유저를 삭제하는 메서드<br> 프로필 이미지, 유저 상태, 유저를 삭제함 <br> DB에 삭제 작업을 진행하기 위해서 트랜잭션을 걸음<br>
   *
   * @param userId
   */
  @Override
  @Transactional
  public void delete(UUID userId) {
    //유저를 찾음, 유저가 없으면 오류 발생
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, userId));

    //유저 이미지가 존재하면 삭제를 진행함
    //Update와 동일한 방식이나, 업데이트에서는 binaryContent 값이 존재할 경우에만 삭제를 진행함
    Optional.ofNullable(user.getProfile().getId())
        .ifPresent(binaryContentService::delete);

    //유저 상태와 유저를 DB에서 제거
    userStatusService.delete(user.getId());
    userRepository.deleteById(user.getId());

    log.info("[UserService] User deleted {}", userId);
  }

  @Override
  public boolean existsById(UUID userId) {
    //값이 존재하는 지 확인하는 메서드
    //추후의 토큰을 위해서 남겨둠
    return userRepository.findById(userId).isPresent();
  }

  //유저의 상태를 바꾸는 메서드
  //컨트롤러단에서 시간정보가 담긴 DTO가 넘어옴
  @Override
  public UserStatusResult online(OnlineUserStatusCommand command) {
    //유저가 존재하는 지 확인, 없으면 오류 발생
    User user = userRepository.findById(command.userId()).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, command.userId())
    );

    // 현재 코드: 유저 엔티티에서 바로 유저 상태를 불러옴
    UserStatus userStatus = user.getUserStatus();
    userStatus.update(command.lastActiveAt());

    return UserStatusResult.create(userStatus);
  }
}
