package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.application.dto.user.UserRequest;
import com.sprint.mission.discodeit.application.dto.user.UserResult;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResult register(UserRequest userRequest, UUID profileId) {
        validateDuplicateEmail(userRequest.email());
        validateDuplicateUserName(userRequest.name());

        User savedUser = userRepository.save(new User(
                userRequest.name(),
                userRequest.email(),
                userRequest.password(),
                profileId)
        );
        userStatusRepository.save(new UserStatus(savedUser.getId()));

        return UserResult.fromEntity(savedUser);
    }

    @Override
    public UserResult findById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        return UserResult.fromEntity(user);
    }

    @Override
    public UserResult findByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        return UserResult.fromEntity(user);
    }

    @Override
    public List<UserResult> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResult::fromEntity)
                .toList();
    }

    @Override
    public UserResult findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));

        return UserResult.fromEntity(user);
    }

    @Override
    public List<UserResult> findAllByIds(List<UUID> userIds) {
        return userIds
                .stream()
                .map(this::findById)
                .toList();
    }

    @Override
    public UserResult updateName(UUID userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));
        user.updateName(name);
        User savedUser = userRepository.save(user);

        return UserResult.fromEntity(savedUser);
    }

    @Override
    public UserResult updateProfileImage(UUID userId, UUID profileId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        user.updateProfileImage(profileId);
        User savedUser = userRepository.save(user);

        return UserResult.fromEntity(savedUser);
    }

    @Override
    public void delete(UUID userId) {
        userRepository.delete(userId);

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        userStatusRepository.delete(userStatus.getId());
    }

    private void validateDuplicateUserName(String name) {
        boolean isDuplicate = userRepository.findByName(name)
                .isPresent();

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 이름 입니다");
        }
    }

    private void validateDuplicateEmail(String requestEmail) {
        boolean isDuplicate = userRepository.findAll()
                .stream()
                .anyMatch(existingUser -> existingUser.isSameEmail(requestEmail));

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
    }
}
