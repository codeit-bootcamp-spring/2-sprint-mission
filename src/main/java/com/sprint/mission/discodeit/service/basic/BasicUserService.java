package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.DTO.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.DTO.User.UserCreateRequest;
import com.sprint.mission.discodeit.DTO.User.UserDto;
import com.sprint.mission.discodeit.DTO.User.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Status;
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
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserDto create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest) {
        // username과 email이 중복되지 않도록 검사
        if (userRepository.existsByUsernameOrEmail(userCreateRequest.username(), userCreateRequest.email())) {
            throw new RuntimeException("Username or email already exists");
        }

        User newUser = new User(userCreateRequest.username(), userCreateRequest.email(), userCreateRequest.password());

        // 프로필 이미지를 등록할 경우
        profileCreateRequest.ifPresent(request -> {
            BinaryContent profileContent = new BinaryContent(request.fileName(), request.contentType(), request.data());
            binaryContentRepository.save(profileContent);
            newUser.setProfileImage(profileContent.getId());
        });

        // UserStatus 생성 및 저장
        UserStatus userStatus = new UserStatus(newUser.getId(), Status.ONLINE);
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);

        // User 저장
        User savedUser = userRepository.save(newUser);

        // UserDto로 변환하여 반환
        return UserDto.fromUser(savedUser, savedUserStatus);
    }

    @Override
    public UserDto find(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User status not found for user ID: " + userId));
        return UserDto.fromUser(user, userStatus);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId()).orElseThrow();
                    return UserDto.fromUser(user, userStatus);
                })
                .collect(Collectors.toList());
    }

    @Override
    public User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest) {
        User existingUser = userRepository.findById(userId).orElseThrow();

        // username과 email이 중복되지 않도록 검사
        if (userUpdateRequest.newUsername() != null && !userUpdateRequest.newUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUsername(userUpdateRequest.newUsername())) {
                throw new RuntimeException("Username already exists");
            }
        }
        if (userUpdateRequest.newEmail() != null && !userUpdateRequest.newEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(userUpdateRequest.newEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }

        existingUser.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), userUpdateRequest.newPassword());

        // 프로필 이미지를 대체할 경우
        profileCreateRequest.ifPresent(request -> {
            BinaryContent newProfileContent = new BinaryContent(request.fileName(), request.contentType(), request.data());
            binaryContentRepository.save(newProfileContent);
            existingUser.setProfileImage(newProfileContent.getId());
        });

        return userRepository.save(existingUser);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow();
        userRepository.delete(user);

        // 관련된 도메인 삭제
        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        userStatusRepository.deleteByUserId(userId);
    }

    @Override
    public boolean exists(UUID authorId) {
        return userRepository.findById(authorId).isPresent();
    }

}

