package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.LogicException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserCreateDto userCreateDto, BinaryContentCreateDto binaryContentCreateDto) {
        if (userRepository.existsByEmail(userCreateDto.email())) {
            throw new LogicException(ErrorCode.USER_EMAIL_EXISTS);
        }

        if (userRepository.existsByUsername(userCreateDto.username())) {
            throw new LogicException(ErrorCode.USER_NAME_EXISTS);
        }

        BinaryContent nullableProfile = null;

        if (binaryContentCreateDto != null) {
            String fileName = binaryContentCreateDto.fileName();
            String contentType = binaryContentCreateDto.contentType();
            byte[] bytes = binaryContentCreateDto.bytes();
            nullableProfile = new BinaryContent(fileName, (long) bytes.length, contentType);
            binaryContentRepository.save(nullableProfile);
            binaryContentStorage.put(nullableProfile.getId(), bytes);
        }

        User newUser = new User(userCreateDto.username(), userCreateDto.email(), userCreateDto.password(),
                nullableProfile);
        UserStatus status = new UserStatus(newUser, Instant.now());
        userRepository.save(newUser);

        return userMapper.toDto(newUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LogicException(ErrorCode.USER_NOT_FOUND));

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LogicException(ErrorCode.USER_NOT_FOUND));

        if (userRepository.existsByEmail(userUpdateDto.newEmail())) {
            throw new LogicException(ErrorCode.USER_EMAIL_EXISTS);
        }

        if (userRepository.existsByUsername(userUpdateDto.newUsername())) {
            throw new LogicException(ErrorCode.USER_NAME_EXISTS);
        }

        BinaryContent nullableProfile = null;

        if (binaryContentCreateDto != null) {
            String fileName = binaryContentCreateDto.fileName();
            String contentType = binaryContentCreateDto.contentType();
            byte[] bytes = binaryContentCreateDto.bytes();
            nullableProfile = new BinaryContent(fileName, (long) bytes.length, contentType);
            binaryContentRepository.save(nullableProfile);
            binaryContentStorage.put(nullableProfile.getId(), bytes);
        }

        user.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), userUpdateDto.newPassword(),
                nullableProfile);

        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LogicException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }
}
