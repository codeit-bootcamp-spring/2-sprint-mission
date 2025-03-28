package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserDto;
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
    public UserDto find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 User를 찾을 수 없음"));
        UserStatus userStatus = userStatusService.findByUserId(user.getId());

        return UserDto.of(user, userStatus);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusService.findByUserId(user.getId());
                    return UserDto.of(user, userStatus);
                }).toList();
    }

    @Override
    public User update(UUID userId, UserUpdateRequest updateRequest,
                       BinaryContentCreateRequest binaryData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(
                        userId + " 에 해당하는 User를 찾을 수 없음"));
        String newUsername = updateRequest.newUsername();
        String newEmail = updateRequest.newEmail();
        String newPassword = updateRequest.newPassword();

        if (!user.getUsername().equals(newUsername)) {
            validDuplicateUsername(newUsername);
        }
        if (!user.getEmail().equals(newEmail)) {
            validDuplicateEmail(newEmail);
        }

        UUID binaryContentId = null;
        if (binaryData != null) {
            if (user.getProfileId() != null) {
                basicBinaryContentService.delete(user.getProfileId());
            }
            binaryContentId = basicBinaryContentService.create(binaryData).getId();
        }
        user.update(newUsername, newEmail, newPassword, binaryContentId);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 에 해당하는 user를 찾을 수 없음"));
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
