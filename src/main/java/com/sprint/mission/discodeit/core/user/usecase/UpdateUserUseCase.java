package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.content.usecase.dto.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UpdateUserCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import java.util.Optional;

public interface UpdateUserUseCase {

  /**
   * <h2>유저 업데이트 메서드</h2>
   * 유저를 업데이트하는 메서드   <br> 유저를 찾은 뒤, 기존 이미지가 존재하고, 또 업데이트할 이미지가 존재하면 기존 이미지를 삭제함 <br> 업데이트를 위해서
   * 트랜잭션을 걸었음
   *
   * @param command          바꿀 대상의 유저 아이디, 새 이름, 새 이메일, 새 패스워드
   * @param binaryContentDTO 바꿀 이미지
   * @return 아이디, 이름, 이메일, 프로필 이미지 메타데이터, 온라인 여부
   */
  UserResult update(UpdateUserCommand command,
      Optional<CreateBinaryContentCommand> binaryContentDTO);

}
