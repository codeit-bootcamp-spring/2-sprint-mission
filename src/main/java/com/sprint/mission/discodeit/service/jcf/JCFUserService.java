package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;
    private final UserStatusService userStatusService;
    private final BasicBinaryContentService basicBinaryContentService;

    public JCFUserService(UserStatusService userStatusService, BasicBinaryContentService basicBinaryContentService) {
        this.data = new HashMap<>();
        this.userStatusService = userStatusService;
        this.basicBinaryContentService = basicBinaryContentService;
    }

    @Override
    public User create(UserCreateRequest createRequest,
                       Optional<BinaryContentCreateRequest> binaryContentRequestNullable) {
        duplicateUsername(createRequest.username());
        duplicateEmail(createRequest.email());
        UUID binaryContentId = binaryContentRequestNullable
                .map(basicBinaryContentService::create).map(BinaryContent::getId).orElse(null);
        User user = new User(createRequest.username(), createRequest.email(), createRequest.password(),
                binaryContentId);
        UserStatusParam statusParam = new UserStatusParam(user.getId(), UserStatusType.ONLINE);
        userStatusService.create(statusParam);

        this.data.put(user.getId(), user);

        return user;
    }

    @Override
    public UserInfoResponse find(UUID userId) {
        User userNullable = this.data.get(userId);
        if (userNullable == null) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        UserStatus userStatus = userStatusService.findByUserId(userNullable.getId());

        return new UserInfoResponse(userNullable.getId(),
                userNullable.getCreatedAt(), userNullable.getUpdatedAt(), userNullable.getUsername(),
                userNullable.getEmail(), userNullable.getProfileId(), userStatus.getStatus());
    }

    @Override
    public List<UserInfoResponse> findAll() {
        return data.values().stream()
                .map(user -> {
                    UserStatus userStatus = userStatusService.findByUserId(user.getId());
                    return new UserInfoResponse(
                            user.getId(), user.getCreatedAt(),
                            user.getUpdatedAt(), user.getUsername(),
                            user.getEmail(), user.getProfileId(),
                            userStatus.getStatus()
                    );
                }).toList();
    }

    @Override
    public User update(UserUpdateRequest updateRequest,
                       Optional<BinaryContentCreateRequest> binaryContentRequestNullable) {
        User userNullable = this.data.get(updateRequest.id());
        if (userNullable == null) {
            throw new NoSuchElementException("User with id " + updateRequest.id() + " not found");
        }

        String newUsername = updateRequest.newUsername().orElse(userNullable.getUsername());
        String newEmail = updateRequest.newEemail().orElse(userNullable.getEmail());
        duplicateUsername(newUsername);
        duplicateEmail(newEmail);

        UUID binaryContentId = binaryContentRequestNullable
                .map(request -> {
                    if (userNullable.getProfileId() != null) {
                        basicBinaryContentService.delete(userNullable.getProfileId());
                    }
                    return basicBinaryContentService.create(request);
                }).map(BinaryContent::getId).orElse(null);

        userNullable.update(
                updateRequest.newUsername().orElse(userNullable.getUsername()),
                updateRequest.newEemail().orElse(userNullable.getEmail()),
                updateRequest.newPassword().orElse(userNullable.getPassword()),
                binaryContentId
        );

        this.data.put(userNullable.getId(), userNullable);
        return userNullable;
    }

    @Override
    public void delete(UUID userId) {
        if (!this.data.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        basicBinaryContentService.delete(userId);
        UserStatus userStatus = userStatusService.findByUserId(userId);
        userStatusService.delete(userStatus.getId());

        this.data.remove(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.data.values().stream()
                .filter(u -> u.getUsername().equals(username)).findFirst();
    }

    private void duplicateUsername(String username) {
        if (existsByUsername(username)) {
            throw new IllegalArgumentException(username + " 은 중복된 username.");
        }
    }

    private void duplicateEmail(String email) {
        if (existsByEmail(email)) {
            throw new IllegalArgumentException(email + " 은 중복된 email.");
        }
    }

    private boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(u -> u.getUsername().equals(username));
    }

    private boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(u -> u.getEmail().equals(email));
    }
}
