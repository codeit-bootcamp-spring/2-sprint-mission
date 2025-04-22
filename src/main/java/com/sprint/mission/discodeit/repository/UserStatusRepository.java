package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

  Optional<UserStatus> findByUserId(UUID userId);

  void deleteByUserId(UUID userId); // 유저 삭제시 함께 삭제하기 위함

  boolean existsByUserId(UUID userId);
}
