package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateParam;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusParam;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusService userStatusService;
    private final BasicBinaryContentService basicBinaryContentService;

    @Override
    public User create(UserCreateRequest createRequest) {
        duplicateUsername(createRequest.getUsername());
        duplicateEmail(createRequest.getEmail());
        UUID binaryContentId = null;

        if (createRequest.getType() != null && createRequest.getFile() != null && !createRequest.getFile().isEmpty()) {
            binaryContentId = profileCreate(createRequest.getType(), List.of(createRequest.getFile()));
        }

        User user = new User(createRequest.getUsername(), createRequest.getEmail(), createRequest.getPassword(),
                binaryContentId);

        UserStatusParam statusParam = new UserStatusParam(user.getId(), UserStatusType.ONLINE);
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
                user.getEmail(), user.getProfileId(), userStatus.getStatus());
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
                            userStatus.getStatus()
                    );
                }).toList();
    }

    @Override
    public User update(UserUpdateRequest updateRequest) {
        duplicateUsername(updateRequest.getNewUsername());
        duplicateEmail(updateRequest.getNewEemail());

        User user = userRepository.findById(updateRequest.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        updateRequest.getId() + " 에 해당하는 userStatus를 찾을 수 없음"));
        UUID binaryContentId = user.getProfileId();

        if (updateRequest.getNewType() != null && updateRequest.getNewFile() != null
                && !updateRequest.getNewFile().isEmpty()) {
            if (binaryContentId != null) {
                basicBinaryContentService.delete(user.getProfileId());
            }
            binaryContentId = profileCreate(updateRequest.getNewType()
                    , List.of(updateRequest.getNewFile()));
        }

        user.update(updateRequest.getNewUsername(), updateRequest.getNewEemail()
                , updateRequest.getNewPassword(), binaryContentId);

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

    private UUID profileCreate(BinaryContentType type, List<MultipartFile> file) {
        BinaryContentCreateParam binaryContentCreateParam = new BinaryContentCreateParam(type, file);
        List<UUID> idList = basicBinaryContentService.create(binaryContentCreateParam);
        return idList.get(0);
    }
}
