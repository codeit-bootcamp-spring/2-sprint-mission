package com.sprint.mission.discodeit.core.user.service;

import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSearchService {

  private final JpaUserRepository userRepository;

  @Cacheable(cacheNames = "users")
  public List<UserDto> findAll() {
    List<User> userList = userRepository.findAllFromDB();
    return userList.stream().map(UserDto::from).toList();
  }

  public List<UserDto> findAllByIdFromDB(List<UUID> ids) {
    List<User> userList = userRepository.findAllByIdsFromDB(ids);
    return userList.stream().map(UserDto::from).toList();
  }
}