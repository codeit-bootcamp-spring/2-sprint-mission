package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    //
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public User create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        String username = userCreateRequest.getUsername();
        String email = userCreateRequest.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User with username " + username + " already exists");
        }

        BinaryContent profile = optionalProfileCreateRequest
                .map(profileRequest -> {
                    BinaryContent binaryContent = BinaryContent.builder()
                            .fileName(profileRequest.getFileName())
                            .size((long)profileRequest.getBytes().length)
                            .contentType(profileRequest.getContentType())
                            .build();

                    BinaryContent saved = binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(saved.getId(), profileRequest.getBytes());
                    return saved;
                })
                .orElse(null);
        String password = userCreateRequest.getPassword();

        User user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .profile(profile)
                .build();
        User createdUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = UserStatus.builder()
                .user(user)
                .lastActiveAt(now)
                .build();
    userStatusRepository.save(userStatus);


        return createdUser;
    }

    @Override
    public UserDto find(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        String newUsername = userUpdateRequest.getNewUsername();
        String newEmail = userUpdateRequest.getNewEmail();
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("User with email " + newEmail + " already exists");
        }
        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("User with username " + newUsername + " already exists");
        }

        BinaryContent profile = optionalProfileCreateRequest
                .map(profileRequest -> {
                    BinaryContent binaryContent = BinaryContent.builder()
                            .fileName(profileRequest.getFileName())
                            .size((long)profileRequest.getBytes().length)
                            .contentType(profileRequest.getContentType())
                            .build();

                    BinaryContent saved = binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(saved.getId(), profileRequest.getBytes());
                    return saved;
                })
                .orElse(null);

        String newPassword = userUpdateRequest.getNewPassword();
        user.update(newUsername, newEmail, newPassword, profile);

        return user;
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        Optional.ofNullable(user.getProfile())
                        .ifPresent(binaryContentRepository::delete);
        userStatusRepository.deleteByUserId(userId);

        userRepository.delete(user);
    }


}
