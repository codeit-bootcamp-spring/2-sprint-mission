package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.binarycontent.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.custom.DuplicateResourceException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    private final UserMapper userMapper;

    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public UserDto create(CreateUserRequest request) {
        validateUserUniqueness(request.username(), request.email());

        User user = User.createUser(
            request.username(), request.email(), request.password());
        User savedUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = UserStatus.createUserStatus(savedUser, now);
        userStatusRepository.save(userStatus);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto create(CreateUserRequest request,
        CreateBinaryContentRequest binaryContentRequest) {

        validateUserUniqueness(request.username(), request.email());

        User user = User.createUser(
            request.username(), request.email(), request.password());
        User savedUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = UserStatus.createUserStatus(savedUser, now);
        savedUser.setStatus(userStatus);
        userStatusRepository.save(userStatus);

        String fileName = binaryContentRequest.fileName();
        String contentType = binaryContentRequest.contentType();
        byte[] bytes = binaryContentRequest.bytes();
        long size = bytes.length;

        BinaryContent binaryContent =
            BinaryContent.createBinaryContent(fileName, size, contentType);
        BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

        binaryContentStorage.put(binaryContent.getId(), bytes);

        BinaryContent oldProfile = user.updateProfile(savedBinaryContent);
        if (oldProfile != null) {
            binaryContentStorage.deleteById(oldProfile.getId());
        }

        return userMapper.toDto(user);
    }

    private void validateUserUniqueness(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("[ERROR] user name already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("[ERROR] email already exists");
        }
    }

    @Override
    @Transactional
    public UserDto findByUserId(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NoSuchElementException("[ERROR] user not found");
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
            .map(userMapper::toDto).toList();
    }

    @Override
    @Transactional
    public UserDto update(UUID userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NoSuchElementException("[ERROR] user not found");
        }
        user.update(request.newUsername(), request.newEmail(), request.newPassword());

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UUID userId, UpdateUserRequest request,
        CreateBinaryContentRequest binaryContentRequest) {

        User user = userRepository.findById(userId).orElseThrow();
        user.update(request.newUsername(), request.newEmail(), request.newPassword());

        String fileName = binaryContentRequest.fileName();
        String contentType = binaryContentRequest.contentType();
        byte[] bytes = binaryContentRequest.bytes();
        long size = bytes.length;

        BinaryContent newProfile =
            BinaryContent.createBinaryContent(fileName, size, contentType);
        binaryContentRepository.save(newProfile);
        BinaryContent oldProfile = user.updateProfile(newProfile);

        binaryContentStorage.put(newProfile.getId(), bytes);
        if (oldProfile != null) {
            binaryContentStorage.deleteById(oldProfile.getId());
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void remove(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();

        BinaryContent profile = user.getProfile();

        binaryContentStorage.deleteById(profile.getId());
        userRepository.delete(user);
        // user status, binary content는 자동으로 삭제
    }
}
