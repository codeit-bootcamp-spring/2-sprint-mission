package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.BinaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.User.UserCreateRequest;
import com.sprint.mission.discodeit.dto.User.UserDto;
import com.sprint.mission.discodeit.dto.User.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFound;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        if (userRepository.existsByUsernameOrEmail(userCreateRequest.username(), userCreateRequest.email())) {
            throw new UserNotFound("Username or email already exists");
        }

        UUID nullableProfileId = profileCreateRequest.map(request -> {
            String fileName = request.fileName();
            String contentType = request.contentType();
            byte[] bytes = request.bytes();
            BinaryContent binaryContent = new BinaryContent(fileName, contentType, bytes, (long) bytes.length);
            return binaryContentRepository.save(binaryContent).getId();
        }).orElse(null);

        String hashedPassword = passwordEncoder.encode(userCreateRequest.password());
        User newUser = new User(username, email, hashedPassword, nullableProfileId);

        User savedUser = userRepository.save(newUser);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(savedUser.getId(), now);
        userStatusRepository.save(userStatus);

        return toDto(savedUser, userStatus);
    }

    @Override
    public UserDto find(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User status not found for user ID: " + userId));
        return toDto(user, userStatus);
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                            .orElse(new UserStatus(user.getId(), null)); // lastActivatedAt은 null로 초기화
                    return toDto(user, userStatus);
                })
                .collect(Collectors.toList());
    }

    @Override
    public User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();

        if (newUsername != null && userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("User with username " + newUsername + " already exists");
        }
        if (newEmail != null && userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("User with email " + newEmail + " already exists");
        }

        UUID nullableProfileId = profileCreateRequest.map(request -> {
            Optional.ofNullable(existingUser.getProfileId())
                    .ifPresent(binaryContentRepository::deleteById);

            String fileName = request.fileName();
            String contentType = request.contentType();
            byte[] bytes = request.bytes();
            BinaryContent binaryContent = new BinaryContent(fileName, contentType, bytes, (long) bytes.length);
            return binaryContentRepository.save(binaryContent).getId();
        }).orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        existingUser.update(newUsername, newEmail, newPassword, nullableProfileId);

        return userRepository.save(existingUser);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);
        userStatusRepository.deleteByUserId(userId);

        userRepository.delete(userId);
    }

    @Override
    public boolean exists(UUID authorId) {
        return userRepository.findById(authorId).isPresent();
    }

    private UserDto toDto(User user, UserStatus userStatus) {
        Boolean online = userStatus != null ? userStatus.isCurrentlyOnline() : null;

        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfileId(),
                user.getEmail(),
                user.getUsername(),
                userStatus.getStatus()
        );
    }

}