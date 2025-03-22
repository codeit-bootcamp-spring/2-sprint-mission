package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusParam;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final BasicBinaryContentService basicBinaryContentService;

    @Override
    public User create(UserCreateRequest createRequest,
                       Optional<BinaryContentCreateRequest> binaryContentRequestNullable) {
        duplicateUsername(createRequest.username());
        duplicateEmail(createRequest.email());
        UUID binaryContentId = binaryContentRequestNullable
                .map(basicBinaryContentService::create).map(BinaryContent::getId).orElse(null);

        User user = new User(createRequest.username(), createRequest.email()
                , createRequest.password(), binaryContentId);

        UserStatusParam statusParam = new UserStatusParam(user.getId(), Instant.now());
        userStatusService.create(statusParam);

        return userRepository.save(user);
    }

    @Override
    public UserInfoResponse find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 userStatus를 찾을 수 없음"));
        UserStatus userStatus = userStatusService.findByUserId(user.getId());

        return new UserInfoResponse(user.getId(),
                user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
                user.getEmail(), user.getProfileId(), userStatus.isOnline());
    }

    @Override
    public List<UserInfoResponse> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusService.findByUserId(user.getId());
                    return new UserInfoResponse(
                            user.getId(), user.getCreatedAt(),
                            user.getUpdatedAt(), user.getUsername(),
                            user.getEmail(), user.getProfileId(),
                            userStatus.isOnline()
                    );
                }).toList();
    }

    @Override
    public User update(UserUpdateRequest updateRequest,
                       Optional<BinaryContentCreateRequest> binaryContentRequestNullable) {
        User user = userRepository.findById(updateRequest.id())
                .orElseThrow(() -> new NoSuchElementException(
                        updateRequest.id() + " 에 해당하는 userStatus를 찾을 수 없음"));

        String newUsername = updateRequest.newUsername().orElse(user.getUsername());
        String newEmail = updateRequest.newEemail().orElse(user.getEmail());
        duplicateUsername(newUsername);
        duplicateEmail(newEmail);

        UUID binaryContentId = binaryContentRequestNullable
                .map(request -> {
                    if (user.getProfileId() != null) {
                        basicBinaryContentService.delete(user.getProfileId());
                    }
                    return basicBinaryContentService.create(request);
                }).map(BinaryContent::getId).orElse(null);

        user.update(
                updateRequest.newUsername().orElse(user.getUsername()),
                updateRequest.newEemail().orElse(user.getEmail()),
                updateRequest.newPassword().orElse(user.getPassword()),
                binaryContentId
        );

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 userStatus를 찾을 수 없음"));
        if (user.getProfileId() != null) {
            basicBinaryContentService.delete(user.getProfileId());
        }
        UserStatus userStatus = userStatusService.findByUserId(userId);
        userStatusService.delete(userStatus.getId());

        userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void duplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(username + " 은 중복된 username.");
        }
    }

    private void duplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(email + " 은 중복된 email.");
        }
    }
}
