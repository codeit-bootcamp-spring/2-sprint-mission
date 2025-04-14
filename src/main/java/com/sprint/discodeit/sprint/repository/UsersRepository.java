package com.sprint.discodeit.sprint.repository;

import com.sprint.discodeit.sprint.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
