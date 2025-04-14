package com.sprint.discodeit.sprint.repository;

import com.sprint.discodeit.sprint.domain.entity.UsersStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UsersStatus, Long> {
}
