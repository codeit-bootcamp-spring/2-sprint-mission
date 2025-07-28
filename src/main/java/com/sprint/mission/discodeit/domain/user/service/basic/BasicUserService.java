package com.sprint.mission.discodeit.domain.user.service.basic;

import static com.sprint.mission.discodeit.common.config.cache.CaffeineCacheConfig.USER_CACHE_NAME;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.mapper.UserResultMapper;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserBinaryContentService userBinaryContentService;
  private final UserResultMapper userResultMapper;
  private final PasswordEncoder passwordEncoder;

  @Caching(
      put = @CachePut(value = USER_CACHE_NAME, key = "#result.id"),
      evict = @CacheEvict(value = USER_CACHE_NAME, key = "'all'")
  )
  @Transactional
  @Override
  public UserResult register(
      UserCreateRequest userRequest,
      BinaryContentRequest binaryContentRequest
  ) {
    validateDuplicateEmail(userRequest.email());
    validateDuplicateUserName(userRequest.username());

    BinaryContent binaryContent = userBinaryContentService.createBinaryContent(binaryContentRequest);

    User user = new User(userRequest.username(), userRequest.email(),
        passwordEncoder.encode(userRequest.password()), binaryContent);
    User savedUser = userRepository.save(user);

    return userResultMapper.convertToUserResult(savedUser);
  }

  @Cacheable(value = USER_CACHE_NAME, key = "#userId")
  @Transactional(readOnly = true)
  @Override
  public UserResult getById(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of()));

    return userResultMapper.convertToUserResult(user);
  }

  @Cacheable(value = USER_CACHE_NAME, key = "#name")
  @Transactional(readOnly = true)
  @Override
  public UserResult getByName(String name) {
    User user = userRepository.findByName(name)
        .orElseThrow(() -> new UserNotFoundException(Map.of("userName", name)));

    return userResultMapper.convertToUserResult(user);
  }

  @Cacheable(value = USER_CACHE_NAME, key = "'all'")
  @Transactional(readOnly = true)
  @Override
  public List<UserResult> getAllIn() {
    return userRepository.findAll()
        .stream()
        .map(userResultMapper::convertToUserResult)
        .toList();
  }

  @Cacheable(value = USER_CACHE_NAME, key = "#email")
  @Transactional(readOnly = true)
  @Override
  public UserResult getByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException(Map.of("userEmail", email)));

    return userResultMapper.convertToUserResult(user);
  }

  @Caching(
      put = @CachePut(value = USER_CACHE_NAME, key = "#userId"),
      evict = @CacheEvict(value = USER_CACHE_NAME, key = "'all'")
  )
  @Transactional
  @Override
  public UserResult update(
      UUID userId,
      UserUpdateRequest userUpdateRequest,
      BinaryContentRequest binaryContentRequest
  ) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of()));
    userBinaryContentService.delete(user.getBinaryContent());

    BinaryContent binaryContent = userBinaryContentService.createBinaryContent(binaryContentRequest);
    user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(),
        passwordEncoder.encode(userUpdateRequest.newPassword()), binaryContent);
    User updatedUser = userRepository.save(user);

    return userResultMapper.convertToUserResult(updatedUser);
  }

  @Caching(evict = {
      @CacheEvict(value = USER_CACHE_NAME, key = "#userId"),
      @CacheEvict(value = USER_CACHE_NAME, key = "'all'")
  })
  @Transactional
  @Override
  public void delete(UUID userId) {
    validateUserExist(userId);

    userRepository.deleteById(userId);
  }

  private void validateDuplicateUserName(String name) {
    if (userRepository.existsUserByName(name)) {
      throw new UserAlreadyExistsException(Map.of("userName", name));
    }
  }

  private void validateDuplicateEmail(String email) {
    if (userRepository.existsUserByEmail(email)) {
      throw new UserAlreadyExistsException(Map.of("email", email));
    }
  }

  private void validateUserExist(UUID userId) {
    if (userRepository.existsById(userId)) {
      return;
    }
    throw new UserNotFoundException(Map.of());
  }

}
