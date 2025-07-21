package com.sprint.mission.discodeit.common.s3;


import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.testutil.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

public class CacheTest extends IntegrationTestSupport {

  @MockitoSpyBean
  private UserRepository userRepository;
  @Autowired
  private UserService userService;

  @AfterEach
  void tearDown() {
    userRepository.deleteAllInBatch();
  }

  @DisplayName("캐시를 적용하면 2번 요청시, DB를 한번만 조회합니다.")
  @Test
  void testCache() {
    // given
    User savedUser = userRepository.save(new User("", "", "", null));

    // when
    userService.getById(savedUser.getId());
    userService.getById(savedUser.getId());

    // then
    Mockito.verify(userRepository, Mockito.times(1))
        .findById(savedUser.getId());
  }

}
