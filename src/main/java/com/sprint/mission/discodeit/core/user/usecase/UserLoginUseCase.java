package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.user.usecase.dto.UserLoginCommand;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;


public interface UserLoginUseCase {

  /**
   * <h2>유저 로그인 메서드</h2>
   * 로그인을 위한 메서드, 이름과 패스워드가 같은 지 확인, 틀리면 throw
   *
   * @param command 유저 이름, 패스워드
   * @return 아이디, 이름, 이메일, 프로필 이미지 메타데이터, 온라인 여부
   */
  UserDto login(UserLoginCommand command);
}
