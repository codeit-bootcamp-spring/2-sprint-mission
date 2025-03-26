package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CreateUserDto;
import com.sprint.mission.discodeit.dto.ReadUserDto;
import com.sprint.mission.discodeit.dto.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.UpdateUserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
    public User create(CreateUserDto dto) {
        UUID profileImageKey = null;
        if (userRepository.existsByName(dto.userName())) {
            throw new IllegalStateException("[Error] 동일한 name");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("[Error] 동일한 email");
        }
        if (dto.profileImageKey() != null) {
            profileImageKey = binaryContentRepository.findByKey(dto.profileImageKey()).getUuid();
        }

        User user = new User(dto.userName(), dto.pwd(), dto.email(), dto.profileImageKey());
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getUuid(), Instant.EPOCH);
        userStatusRepository.save(userStatus);

        return user;
    }
    @Override
    public ReadUserDto read(UUID userKey) {
        User user = userRepository.findByKey(userKey);
        UserStatus userStatus = userStatusRepository.findByUserKey(user.getUuid());
        boolean isOnline = userStatus.isOnline();

        if (user.getUuid() == null) {
            throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
        }

        return new ReadUserDto(user.getUuid(), user.getName(), user.getEmail(), isOnline);
    }

    @Override
    public List<ReadUserDto> readAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new IllegalArgumentException("[Error] 조회할 사용자가 존재하지 않습니다.");
        }

        return users.stream().map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserKey(user.getUuid());
                    boolean isOnline = userStatus.isOnline();
                    return new ReadUserDto(user.getUuid(), user.getName(), user.getEmail(), isOnline);
                })
                .toList();
    }

    @Override
    public UpdateUserResponseDto update(UpdateUserRequestDto request) {
        User user = userRepository.findByKey(request.userKey());

        if (user == null) {
            throw new IllegalStateException("[Error] user not found");
        }
        if (!request.userName().isEmpty()) {
            user.updatePwd(request.userName());
        }
        if (!request.pwd().isEmpty()) {
            user.updatePwd(request.pwd());
        }
        if (!request.email().isEmpty()) {
            user.updateEmail(request.email());
        }

        userRepository.save(user);

        UUID updatedProfileImageKey = user.getProfileId();

        if (request.profileKey() != null && !request.profileKey().equals(user.getProfileId())) {
            if (!binaryContentRepository.existsByKey(request.profileKey())) {
                throw new IllegalArgumentException("[Error] 존재하지 않는 프로필 이미지입니다.");
            }
            if (updatedProfileImageKey != null) {
                binaryContentRepository.delete(updatedProfileImageKey);
            }
            updatedProfileImageKey = request.profileKey();
        }

        return new UpdateUserResponseDto(user.getName(), user.getPwd(), user.getEmail(), updatedProfileImageKey);
    }

    @Override
    public void delete(UUID userKey) {
        User user = userRepository.findByKey(userKey);

        if (user == null) {
            throw new IllegalStateException("[Error] 유저가 존재하지 않습니다.");
        }
        UUID profileId = user.getProfileId();

        binaryContentRepository.delete(profileId);
        userStatusService.deleteByUserKey(user.getUuid());
        userRepository.delete(user);
    }
}
