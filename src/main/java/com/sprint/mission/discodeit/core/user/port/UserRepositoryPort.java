package com.sprint.mission.discodeit.core.user.port;

import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepositoryPort {

  User save(User user);

  Optional<User> findById(UUID id);

  List<User> findAll();

  void delete(UUID id);

  boolean existId(UUID id);

  boolean existName(String name);

  boolean existEmail(String email);

}

// 하나로 묶은 서비스 인터페이스 만들어 되지만
// Repository 하나의 레포지토리를 다루는 것이라서 안 쪼갬
// UseCase 쪼갠 것은 별 의미는 없다.
