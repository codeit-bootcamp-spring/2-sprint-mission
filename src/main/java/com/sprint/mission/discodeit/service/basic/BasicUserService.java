package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.file.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User createUser(CreateUserRequest request,
        Optional<CreateBinaryContentRequest> profileOpt) {
        if (userRepository.getAllUsers().stream().anyMatch(
            user -> user.getUsername().equals(request.username()) || user.getEmail()
                .equals(request.email())
        )) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        UUID profileId = profileOpt.map(profile -> {
            BinaryContent binaryContent = new BinaryContent(
                profile.fileName(),
                (long) profile.bytes().length,
                profile.contentType(),
                profile.bytes()
            );
            return binaryContentRepository.save(binaryContent).getId();
        }).orElse(null);

        User user = new User(
            request.username(),
            request.password(),
            request.email(),
            profileId
        );
        userRepository.save(user);

        userStatusRepository.save(new UserStatus(user.getId()));

        return user;
    }

    @Override
    public Optional<UserResponse> getUserById(UUID userId) {
        return userRepository.getUserById(userId)
            .map(user -> new UserResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatusRepository.getById(userId).online(),
                user.getPassword()
            ));
    }

    @Override
    public List<UserResponse> getUsersByName(String name) {
        return userRepository.getAllUsers().stream()
            .filter(user -> user.getUsername().equals(name))
            .map(user -> new UserResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatusRepository.getById(user.getId()).online(),
                user.getPassword()
            ))
            .toList();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.getAllUsers().stream()
            .map(user -> new UserResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatusRepository.getById(user.getId()).online(),
                user.getPassword()
            ))
            .toList();
    }

    @Override
    public void updateUser(UpdateUserRequest request,
        Optional<CreateBinaryContentRequest> profileOpt) {
        userRepository.getUserById(request.userId()).ifPresent(user -> {
            Instant updatedTime = Instant.now();

            UUID newProfileId = profileOpt.map(profile -> {
                Optional.ofNullable(user.getProfileId())
                    .ifPresent(binaryContentRepository::deleteById);

                BinaryContent binaryContent = new BinaryContent(
                    profile.fileName(),
                    (long) profile.bytes().length,
                    profile.contentType(),
                    profile.bytes()
                );

                return binaryContentRepository.save(binaryContent).getId();
            }).orElse(user.getProfileId());

            user.update(
                request.newUsername(),
                request.newPassword(),
                request.newEmail(),
                updatedTime,
                newProfileId
            );

            userRepository.save(user);
        });
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.getUserById(userId).ifPresent(user -> {
            if (user.getProfileId() != null) {
                binaryContentRepository.deleteById(user.getProfileId());
            }
        });
        userStatusRepository.deleteById(userId);
        userRepository.deleteUser(userId);
    }
}
