package com.sprint.mission.discodeit.core.user.usecase;

import java.util.UUID;

public interface DeleteUserUseCase {

  /**
   * <h2>유저 제거 메서드</h2>
   * 유저를 삭제하는 메서드<br> 프로필 이미지, 유저 상태, 유저를 삭제함 <br> DB에 삭제 작업을 진행하기 위해서 트랜잭션을 걸음<br>
   *
   * @param userId
   */
  void delete(UUID userId);

}
