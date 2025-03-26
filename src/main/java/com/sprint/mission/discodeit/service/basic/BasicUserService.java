package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusCreateRequest;
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
                       BinaryContentCreateRequest binaryData) {
        validDuplicateUsername(createRequest.username());
        validDuplicateEmail(createRequest.email());

        UUID binaryContentId = (binaryData != null)
                ? basicBinaryContentService.create(binaryData).getId()
                : null;

        User user = new User(createRequest.username(), createRequest.email()
                , createRequest.password(), binaryContentId);
        userRepository.save(user);
        UserStatusCreateRequest statusParam = new UserStatusCreateRequest(user.getId());
        userStatusService.create(statusParam);

        return user;
    }

    @Override
    public UserInfoResponse find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 User를 찾을 수 없음"));
        UserStatus userStatus = userStatusService.findByUserId(user.getId());

        return UserInfoResponse.of(user, userStatus);
    }

    @Override
    public List<UserInfoResponse> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusService.findByUserId(user.getId());
                    return UserInfoResponse.of(user, userStatus);
                }).toList();
    }

    @Override
    public User update(UserUpdateRequest updateRequest,
                       BinaryContentCreateRequest binaryData) {
        User user = userRepository.findById(updateRequest.id())
                .orElseThrow(() -> new NoSuchElementException(
                        updateRequest.id() + " 에 해당하는 userStatus를 찾을 수 없음"));
        validDuplicateUsername(updateRequest.newUsername());
        validDuplicateEmail(updateRequest.newEmail());

        String username = (updateRequest.newUsername() == null) ? user.getUsername() : updateRequest.newUsername();
        String email = (updateRequest.newEmail() == null) ? user.getEmail() : updateRequest.newEmail();
        String password = (updateRequest.newPassword() == null) ? user.getPassword() : updateRequest.newPassword();

        UUID binaryContentId = null;
        if (binaryData != null) {
            if (user.getProfileId() != null) {
                basicBinaryContentService.delete(user.getProfileId());
            }
            binaryContentId = basicBinaryContentService.create(binaryData).getId();
        }
        user.update(username, email, password, binaryContentId);
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

    private void validDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(username + " 은 중복된 username.");
        }
    }

    private void validDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(email + " 은 중복된 email.");
        }
    }
}
