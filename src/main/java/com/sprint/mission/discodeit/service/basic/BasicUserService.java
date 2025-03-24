package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    public UserResponse create(UserCreateRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent() ||
                userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 username 또는 email입니다.");
        }

        User user = new User(request.username(), request.email(), request.password());
        userRepository.save(user);

        if (request.profileImage() != null) {
            BinaryContent profileImage = new BinaryContent(user.getId(), null);
            binaryContentRepository.save(profileImage);
        }

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), userStatus.isOnline());
    }

    public Optional<UserResponse> find(UUID userId) {
        return userRepository.findById(userId).map(user -> {
            boolean isOnline = userStatusRepository.findByUserId(user.getId())
                    .map(UserStatus::isOnline).orElse(false);
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), isOnline);
        });
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().parallelStream()
                .map(user -> {
                    boolean isOnline = userStatusRepository.findByUserId(user.getId())
                            .map(UserStatus::isOnline).orElse(false);
                    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), isOnline);
                }).collect(Collectors.toList());
    }

    public void update(UserUpdateRequest request) {
        User user = userRepository.findById(request.id()).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        user.update(request.username(), request.email(), request.password());
        userRepository.save(user);

        if (request.profileImage() != null) {
            binaryContentRepository.findByUserId(user.getId())
                    .forEach(image -> binaryContentRepository.deleteById(image.getId()));

            BinaryContent profileImage = new BinaryContent(user.getId(), null);
            binaryContentRepository.save(profileImage);
        }
    }

    public void delete(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        userStatusRepository.findByUserId(userId).ifPresent(status -> userStatusRepository.deleteById(status.getId()));
        binaryContentRepository.findByUserId(userId).forEach(image -> binaryContentRepository.deleteById(image.getId()));

        userRepository.deleteById(userId);
    }
}

