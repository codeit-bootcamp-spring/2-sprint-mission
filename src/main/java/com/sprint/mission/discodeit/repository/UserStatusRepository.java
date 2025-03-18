package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    Optional<UserStatus> findById(UUID id);
    UserStatus findByUserId(UUID userId); // user가 생성되면서 같이 생성되므로 Optional 필요 X
    List<UserStatus> findAll();
    void deleteById(UUID id);
    void deleteByUserId(UUID userId); // 유저 삭제시 함께 삭제하기 위함
    boolean existsByUserId(UUID userId);

}
