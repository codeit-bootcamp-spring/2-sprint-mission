package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateParam;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

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
    public User create(UserCreateRequest createParam) {
        duplicateUsername(createParam.getUsername());
        duplicateEmail(createParam.getEmail());
        UUID binaryContentId = null;

        if (createParam.getType() != null && createParam.getFile() != null && !createParam.getFile().isEmpty()) {
            binaryContentId = profileCreate(createParam.getType(), List.of(createParam.getFile()));
        }

        User user = new User(createParam.getUsername(), createParam.getEmail(), createParam.getPassword(),
                binaryContentId);
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
        return data.entrySet().stream()
                .map(entry -> {
                    User user = entry.getValue();
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
    public User update(UserUpdateRequest updateParam) {
        duplicateUsername(updateParam.getNewUsername());
        duplicateEmail(updateParam.getNewEemail());

        User userNullable = this.data.get(updateParam.getId());
        if (userNullable == null) {
            throw new NoSuchElementException("User with id " + updateParam.getId() + " not found");
        }

        UUID binaryContentId = userNullable.getProfileId();

        if (updateParam.getNewType() != null && updateParam.getNewFile() != null
                && !updateParam.getNewFile().isEmpty()) {
            if (binaryContentId != null) {
                basicBinaryContentService.delete(userNullable.getProfileId());
            }
            binaryContentId = profileCreate(updateParam.getNewType()
                    , List.of(updateParam.getNewFile()));
        }

        userNullable.update(updateParam.getNewUsername(), updateParam.getNewEemail()
                , updateParam.getNewPassword(), binaryContentId);

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

    private UUID profileCreate(BinaryContentType type, List<MultipartFile> file) {
        BinaryContentCreateParam binaryContentCreateParam = new BinaryContentCreateParam(type, file);
        List<UUID> idList = basicBinaryContentService.create(binaryContentCreateParam);
        return idList.get(0);
    }

    private boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(u -> u.getUsername().equals(username));
    }

    private boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(u -> u.getEmail().equals(email));
    }
}
