package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusService userStatusService;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserDto create(UserCreateRequest request) {
        UUID profileImageKey = null;
        if (userRepository.existsByName(request.username())) {
            throw new IllegalStateException("[Error] 동일한 name");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("[Error] 동일한 email");
        }
        if (request.profileImageKey() != null) {
            profileImageKey = binaryContentRepository.findByKey(request.profileImageKey()).getUuid();
        }

        User user = new User(request.username(), request.password(), request.email(), profileImageKey);
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getUuid(), Instant.EPOCH);
        userStatusRepository.save(userStatus);

        return new UserDto(user.getUuid(), user.getCreatedAt(), user.getUpdatedAt(), user.getName(), user.getEmail(), user.getProfileId(), userStatus.isOnline());
    }
    @Override
    public UserDto read(UUID userKey) {
        User user = userRepository.findByKey(userKey);
        UserStatus userStatus = userStatusRepository.findByUserKey(user.getUuid());
        boolean isOnline = userStatus.isOnline();

        if (user.getUuid() == null) {
            throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
        }

        return new UserDto(user.getUuid(), user.getCreatedAt(), user.getUpdatedAt(), user.getName(), user.getEmail(), user.getProfileId(), userStatus.isOnline());
    }

    @Override
    public List<UserDto> readAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
        }
        return users.stream().map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserKey(user.getUuid());
                    return new UserDto(user.getUuid(), user.getCreatedAt(), user.getUpdatedAt(), user.getName(), user.getEmail(), user.getProfileId(), userStatus.isOnline());
                })
                .toList();
    }

    @Override
    public User update(UserUpdateRequest request) {
        User user = userRepository.findByKey(request.userKey());

        if (user == null) {
            throw new IllegalStateException("[Error] user not found");
        }
        if (!request.newUsername().isEmpty()) {
            user.updatePwd(request.newUsername());
        }
        if (!request.newPassword().isEmpty()) {
            user.updatePwd(request.newPassword());
        }
        if (!request.newEmail().isEmpty()) {
            user.updateEmail(request.newEmail());
        }

        UUID updatedProfileImageKey = user.getProfileId();

        if (request.newProfileKey() != null && !request.newProfileKey().equals(user.getProfileId())) {
            if (!binaryContentRepository.existsByKey(request.newProfileKey())) {
                throw new IllegalArgumentException("[Error] 존재하지 않는 프로필 이미지입니다.");
            }
            if (updatedProfileImageKey != null) {
                binaryContentRepository.delete(updatedProfileImageKey);
            }
            updatedProfileImageKey = request.newProfileKey();
        }

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userKey) {
        User user = userRepository.findByKey(userKey);

        if (user == null) {
            throw new IllegalStateException("[Error] 유저가 존재하지 않습니다.");
        }
        if (user.getProfileId() != null) {
            binaryContentRepository.delete(user.getProfileId());
        }
        userStatusService.deleteByUserKey(userKey);
        userRepository.delete(userKey);
    }
}
