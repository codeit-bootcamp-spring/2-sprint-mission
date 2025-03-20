package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserRequest;
import com.sprint.mission.discodeit.dto.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
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

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User createUser(CreateUserRequest request) {
        if (userRepository.getAllUsers().stream().anyMatch(
                user -> user.getUsername().equals(request.username()) || user.getEmail().equals(request.email())
        )) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = new User(request.username(), request.password(), request.email(), null);
        userRepository.save(user);

        if (request.profileImageFileName() != null && request.profileImageFilePath() != null) {
            BinaryContent profileImage = new BinaryContent(
                    user.getId(),
                    null,
                    request.profileImageFileName(),
                    request.profileImageFilePath()
            );
            binaryContentRepository.save(profileImage);
            user.setProfileId(profileImage.getId());  // User의 profileId 업데이트
            userRepository.save(user);
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public Optional<UserResponse> getUserById(UUID userId) {
        return userRepository.getUserById(userId)
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        userStatusRepository.getById(userId)
                                .map(UserStatus::isOnline)
                                .orElse(false)
                ));
    }

    @Override
    public List<UserResponse> getUsersByName(String name) {
        return userRepository.getAllUsers().stream()
                .filter(user -> user.getUsername().equals(name))
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        userStatusRepository.getById(user.getId())
                                .map(UserStatus::isOnline).orElse(false)
                ))
                .toList();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        userStatusRepository.getById(user.getId())
                                .map(UserStatus::isOnline).orElse(false)
                ))
                .toList();
    }

    @Override
    public void updateUser(UpdateUserRequest request) {
        userRepository.getUserById(request.userId()).ifPresent(user -> {
            Instant updatedTime = Instant.now();
            user.update(request.newName(), user.getPassword(), request.newEmail(), updatedTime);
            userRepository.save(user);

            if (request.profileImageFileName() != null && request.profileImageFilePath() != null) {
                BinaryContent profileImage = new BinaryContent(
                        user.getId(),
                        null,
                        request.profileImageFileName(),
                        request.profileImageFilePath()
                );
                binaryContentRepository.save(profileImage);
                user.setProfileId(profileImage.getId());
                userRepository.save(user);
            }
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
