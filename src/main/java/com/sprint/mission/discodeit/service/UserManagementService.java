package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.BinaryContentRepositoryImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserManagementService {
    private final UserRepository userRepository;
    private final BinaryContentRepositoryImpl binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    public UserManagementService(UserRepository userRepository, BinaryContentRepositoryImpl binaryContentRepository, UserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.binaryContentRepository = binaryContentRepository;
        this.userStatusRepository = userStatusRepository;
    }

    // 유저 생성
    public void createUser(CreateUserDTO request) {
        if (userRepository.existsById(UUID.randomUUID())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User(request.getUserName(), request.getEmail(), request.getPassword(), request.getProfileId());
        userRepository.save(user);

        if (request.getProfileId() != null) {
            binaryContentRepository.save(new BinaryContent(request.getProfileId(), user.getId(), null, new byte[0]));
        }

        UserStatus userStatus = new UserStatus(UUID.randomUUID(), user.getId(), "ONLINE", Instant.now());
        userStatusRepository.save(userStatus);
    }

    // 유저 조회
    public Optional<User> findUserById(UUID userId) {
        return userRepository.findById(userId).map(user -> {
            // 🔹 유저 상태 조회
            Optional<UserStatus> statusOpt = userStatusRepository.findByUserId(user.getId());
            boolean isOnline = statusOpt.map(UserStatus::isOnline).orElse(false);

            // 🔹 패스워드 정보 제외
            return new User(user.getUsername(), user.getEmail(), null, user.getProfileId());
        });
    }

    // 전체 유저 조회
    public List<User> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new User(user.getUsername(), user.getEmail(), null, user.getProfileId()))
                .toList();
    }

    // 유저 정보 업데이트
    public void updateUser(UUID userId, UpdateUserDTO request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }

        User user = userOpt.get();
            user.update(request.getUserName(), request.getEmail(), request.getPassword(), request.getProfileId());
        userRepository.save(user);
    }

    // 유저 삭제
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found.");
        }

        // ✅ 관련된 데이터 삭제
        binaryContentRepository.deleteByUserId(userId);
        userStatusRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
}
