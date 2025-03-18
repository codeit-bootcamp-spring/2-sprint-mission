package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserRequest;
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
                user -> user.getName().equals(request.name()) || user.getEmail().equals(request.email())
        )) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = new User(request.name(), request.email());
        userRepository.save(user);

        if (request.profileImageFileName() != null && request.profileImageFilePath() != null) {
            BinaryContent profileImage = new BinaryContent(
                    user.getId(),
                    request.profileImageFileName(),
                    request.profileImageFilePath()
            );
            binaryContentRepository.save(profileImage);
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public List<User> getUsersByName(String name) {
        return userRepository.getAllUsers().stream()
                .filter(user -> user.getName().equals(name))
                .toList();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void updateUser(UUID userId, String newName, String newEmail) {
        userRepository.getUserById(userId).ifPresent(user -> {
            Instant updatedTime = Instant.now();
            user.update(newName, newEmail, updatedTime);
            userRepository.save(user);
        });
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteUser(userId);
    }
}
