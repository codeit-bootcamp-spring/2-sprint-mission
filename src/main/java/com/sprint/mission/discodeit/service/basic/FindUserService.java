package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// AOP self-invocation을 해결하기 위한 별도의 클래스
@Service
@RequiredArgsConstructor
public class FindUserService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  @Cacheable(value = "allUsers")
  public List<User> findAllUserOnly() {
    return userRepository.findAllFetch();
  }
}
