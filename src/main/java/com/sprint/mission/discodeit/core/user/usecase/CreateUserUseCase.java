package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.CreateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import java.util.Optional;

public interface CreateUserUseCase {

  /**
   * <h2>유저 생성 메서드</h2>
   * 유저를 생성한다. <br> 동일한 이름, 이메일이 존재하면 오류를 발생, <br> 내부에서 바이너리 데이터, 유저 상태를 생성한다.
   *
   * @param command          유저 이름, 이메일, 패스워드
   * @param binaryContentDTO 파일 이름, 파일 타입, 바이트
   * @return 아이디, 이름, 이메일, 프로필 이미지 메타데이터, 온라인 여부
   */
  UserDto create(CreateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO);

}
