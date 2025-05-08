package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserMapper userMapper;

    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
                          Optional<BinaryContentCreateRequest> profileCreateRequest) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with username " + username + " already exists");
        }

        BinaryContent profile = profileCreateRequest
                .map(request -> {
                    String fileName = request.fileName();
                    String contentType = request.contentType();
                    byte[] bytes = request.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                            contentType);
                    binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(binaryContent.getId(), bytes);
                    return binaryContent;
                })
                .orElse(null);
        String password = userCreateRequest.password();

        User user = new User(username, email, password, profile);

        UserStatus userStatus = new UserStatus(user, Instant.now());
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto searchUser(UUID userId) {
        User user = getUser(userId);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest) {
        User user = getUser(userId);

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();

        if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("User with email " + newEmail + " already exists");
        }
        if (!newUsername.equals(user.getUsername()) && userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("User with username " + newUsername + " already exists");
        }

        BinaryContent profile = profileCreateRequest
                .map(request -> {

                    String fileName = request.fileName();
                    String contentType = request.contentType();
                    byte[] bytes = request.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                            contentType);
                    binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(binaryContent.getId(), bytes);
                    return binaryContent;
                })
                .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        user.updateUser(newUsername, newEmail, newPassword, profile);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        User user = getUser(userId);
        userRepository.delete(user);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }


}
