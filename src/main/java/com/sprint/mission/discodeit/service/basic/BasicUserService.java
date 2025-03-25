package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserDTO;
import com.sprint.mission.discodeit.dto.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.user.UserResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User createUser(CreateUserDTO dto) {
        if (isUserNameExists(dto.userName())) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }
        if (isEmailExists(dto.email())) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }
        User newUser = new User(dto.userName(), dto.email(), dto.password());

        if (dto.profileId() != null) {
            newUser.setProfileId(dto.profileId());
        }

        User user = userRepository.save(newUser);

        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.saveUserStatus(userStatus);
        return user;
    }

    @Override
    public UserResponseDTO searchUser(UUID userId) {
        User user = getUser(userId);
        boolean isOnline = userStatusRepository.isUserOnline(user.getId());

        return new UserResponseDTO(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileId(),
                isOnline
        );
    }

    @Override
    public List<UserResponseDTO> searchAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    boolean isOnline = userStatusRepository.isUserOnline(user.getId()); // 각 사용자 온라인 상태 조회
                    return new UserResponseDTO(
                            user.getId(),
                            user.getCreatedAt(),
                            user.getUpdatedAt(),
                            user.getUserName(),
                            user.getEmail(),
                            user.getProfileId(),
                            isOnline
                    );
                })
                .toList();
    }

    @Override
    public User updateUser(UpdateUserDTO dto) {
        User user = getUser(dto.userId());
        user.updateUser(dto.userName(), dto.email(), dto.password(), dto.profileId());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = getUser(userId);
        if(user.getProfileId() != null) {
            binaryContentRepository.delete(user.getProfileId());
        }
        userStatusRepository.delete(userId);
        userRepository.delete(userId);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
    }

    private boolean isUserNameExists(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }

    private boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
