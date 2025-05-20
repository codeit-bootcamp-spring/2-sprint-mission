package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateUserException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    public UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        log.info("▶▶ [SERVICE] Creating user - username: {}, email: {}", username, email);

        if (userRepository.existsByEmail(email)) {
            log.warn("◀◀ [SERVICE] User with email already exists - email: {}", email);
            throw new DuplicateUserException("email", email);
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("◀◀ [SERVICE] User with username already exists - username: {}", username);
            throw new DuplicateUserException("username", username);
        }

        BinaryContent profileContent = optionalProfileCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();

                    log.info("▶▶ [SERVICE] Creating profile binaryContent for user - fileName: {}", fileName);

                    BinaryContent binaryContent = BinaryContent.builder()
                            .fileName(fileName)
                            .size((long) bytes.length)
                            .contentType(contentType)
                            .build();

                    BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

                    binaryContentStorage.put(savedBinaryContent.getId(), bytes);

                    log.info("◀◀ [SERVICE] Profile binaryContent created and saved - id: {}", savedBinaryContent.getId());
                    return savedBinaryContent;
                })
                .orElse(null);

        String password = userCreateRequest.password();

        User user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .profile(profileContent)
                .build();

        User createdUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = UserStatus.builder()
                .user(createdUser)
                .lastActiveAt(now)
                .build();
        userStatusRepository.save(userStatus);

        log.info("◀◀ [SERVICE] User created successfully - id: {}", createdUser.getId());
        return userMapper.toDto(createdUser);
    }

    @Override
    public UserDto find(UUID userId) {
        log.info("▶▶ [SERVICE] Finding user - id: {}", userId);
        return userRepository.findById(userId)
                .map(user -> {
                    log.info("◀◀ [SERVICE] User found - id: {}", userId);
                    return userMapper.toDto(user);
                })
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] User not found - id: {}", userId);
                    return new UserNotFoundException(userId);
                });
    }

    @Override
    public List<UserDto> findAll() {
        log.info("▶▶ [SERVICE] Finding all users");
        List<User> users = userRepository.findAll();
        log.info("◀◀ [SERVICE] Found {} users", users.size());
        return userMapper.toDtoList(users);
    }

    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        log.info("▶▶ [SERVICE] Updating user - id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] User not found for update - id: {}", userId);
                    return new UserNotFoundException(userId);
                });

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        if (newEmail != null && !newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
            log.warn("◀◀ [SERVICE] Email already in use during update - email: {}", newEmail);
            throw new DuplicateUserException("email", newEmail);
        }
        if (newUsername != null && !newUsername.equals(user.getUsername()) && userRepository.existsByUsername(newUsername)) {
            log.warn("◀◀ [SERVICE] Username already in use during update - username: {}", newUsername);
            throw new DuplicateUserException("username", newUsername);
        }

        optionalProfileCreateRequest.ifPresent(profileRequest -> {
            if (user.getProfile() != null) {
                UUID profileId = user.getProfile().getId();
                log.info("▶▶ [SERVICE] Removing old profile binaryContent - id: {}", profileId);
                binaryContentRepository.deleteById(profileId);
            }

            String fileName = profileRequest.fileName();
            String contentType = profileRequest.contentType();
            byte[] bytes = profileRequest.bytes();

            log.info("▶▶ [SERVICE] Creating new profile binaryContent for user update - fileName: {}", fileName);

            BinaryContent newProfile = BinaryContent.builder()
                    .fileName(fileName)
                    .size((long) bytes.length)
                    .contentType(contentType)
                    .build();

            BinaryContent savedProfile = binaryContentRepository.save(newProfile);
            binaryContentStorage.put(savedProfile.getId(), bytes);
            user.updateProfile(savedProfile);

            log.info("◀◀ [SERVICE] New profile binaryContent created and saved - id: {}", savedProfile.getId());
        });

        if (newUsername != null) {
            user.setUsername(newUsername);
        }
        if (newEmail != null) {
            user.setEmail(newEmail);
        }
        if (userUpdateRequest.newPassword() != null) {
            user.setPassword(userUpdateRequest.newPassword());
        }

        log.info("◀◀ [SERVICE] User updated successfully - id: {}", userId);
        return userMapper.toDto(user);
    }

    @Override
    public void delete(UUID userId) {
        log.info("▶▶ [SERVICE] Deleting user - id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("◀◀ [SERVICE] User not found for deletion - id: {}", userId);
                    return new UserNotFoundException(userId);
                });

        if (user.getProfile() != null) {
            log.info("▶▶ [SERVICE] Deleting user's profile binaryContent - id: {}", user.getProfile().getId());
            binaryContentRepository.deleteById(user.getProfile().getId());
        }

        userRepository.deleteById(userId);
        log.info("◀◀ [SERVICE] User deleted successfully - id: {}", userId);
    }
}