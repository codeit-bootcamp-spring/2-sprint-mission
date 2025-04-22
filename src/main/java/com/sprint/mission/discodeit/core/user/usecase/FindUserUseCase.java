package com.sprint.mission.discodeit.core.user.usecase;

import com.sprint.mission.discodeit.core.user.usecase.dto.UserResult;
import java.util.List;
import java.util.UUID;

public interface FindUserUseCase {

  /**
   * <h2>유저 찾기 메서드</h2>
   * id를 통해서 유저를 찾음
   *
   * @param userId
   * @return 아이디, 이름, 이메일, 프로필 이미지 메타데이터, 온라인 여부
   */
  UserResult findById(UUID userId);

  boolean existsById(UUID userId);

  /**
   * <h2>유저 전체 찾기 메서드</h2>
   * 등록된 모든 유저를 출력함
   *
   * @return List [아이디, 이름, 이메일, 프로필 이미지 메타데이터, 온라인 여부]
   */
  List<UserResult> findAll();

}
