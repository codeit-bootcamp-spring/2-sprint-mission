package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserEmailExistsException;
import com.sprint.mission.discodeit.exception.user.UserNameExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserCreateDto userCreateDto, BinaryContentCreateDto binaryContentCreateDto) {
        log.info("Creating user: {}", userCreateDto);

        if (userRepository.existsByEmail(userCreateDto.email())) {
            log.warn("User email {} already exists", userCreateDto.email());
            throw new UserEmailExistsException(userCreateDto.email());
        }

        if (userRepository.existsByUsername(userCreateDto.username())) {
            log.warn("User username {} already exists", userCreateDto.username());
            throw new UserNameExistsException(userCreateDto.username());
        }

        BinaryContent nullableProfile = null;

        if (binaryContentCreateDto != null) {
            String fileName = binaryContentCreateDto.fileName();
            String contentType = binaryContentCreateDto.contentType();
            byte[] bytes = binaryContentCreateDto.bytes();
            nullableProfile = new BinaryContent(fileName, (long) bytes.length, contentType);
            binaryContentRepository.save(nullableProfile);
            binaryContentStorage.put(nullableProfile.getId(), bytes);
            log.debug("Profile created: {}", nullableProfile);
        }

        User newUser = new User(userCreateDto.username(), userCreateDto.email(), userCreateDto.password(),
                nullableProfile);
        UserStatus status = new UserStatus(newUser, Instant.now());
        userRepository.save(newUser);

        log.info("User saved successfully: {}", newUser);
        return userMapper.toDto(newUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAllWithProfileAndStatus().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateDto userUpdateDto,
                          BinaryContentCreateDto binaryContentCreateDto) {
        log.info("Updating user: userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User ID {} not found", userId);
                    return new UserNotFoundException(userId);
                });

        if (userRepository.existsByEmail(userUpdateDto.newEmail())) {
            log.warn("User email {} already exists", userUpdateDto.newEmail());
            throw new UserEmailExistsException(userUpdateDto.newEmail());
        }

        if (userRepository.existsByUsername(userUpdateDto.newUsername())) {
            log.warn("User username {} already exists", userUpdateDto.newUsername());
            throw new UserNameExistsException(userUpdateDto.newUsername());
        }

        BinaryContent nullableProfile = null;

        if (binaryContentCreateDto != null) {
            String fileName = binaryContentCreateDto.fileName();
            String contentType = binaryContentCreateDto.contentType();
            byte[] bytes = binaryContentCreateDto.bytes();
            nullableProfile = new BinaryContent(fileName, (long) bytes.length, contentType);
            binaryContentRepository.save(nullableProfile);
            binaryContentStorage.put(nullableProfile.getId(), bytes);
            log.debug("Profile updated: {}", nullableProfile);
        }

        user.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), userUpdateDto.newPassword(),
                nullableProfile);

        log.info("User updated successfully: {}", user);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        log.info("Deleting user: userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.delete(user);
        log.info("User deleted successfully: userId={}", userId);
    }
}
