package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreate;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdate;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserCreate dto) {
        Map<UUID, User> userData = userRepository.getUserData();

        if (userData.values().stream()
                .anyMatch(user -> user.getUsername().equals(dto.getUsername()))) {
            throw new IllegalArgumentException("같은 이름을 가진 사람이 있습니다.");
        }
        if (userData.values().stream()
                .anyMatch(user -> user.getEmail().equals(dto.getEmail()))) {
            throw new IllegalArgumentException("같은 메일을 가진 사람이 있습니다.");
        }

        User user = new User(dto.getUsername(), dto.getEmail(), dto.getPassword(), dto.getProfileId());
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        return user;
    }

    @Override
    public UserResponse find(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        User user = userRepository.findById(userId);

        return new UserResponse(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
                user.getEmail(), user.getProfileId(), userStatus.isOnline());
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(
                        user -> {
                            UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
                            return new UserResponse(user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
                                    user.getUsername(),
                                    user.getEmail(), user.getProfileId(), userStatus.isOnline());
                        }).toList();
    }

    @Override //여기서부터 시작
    public User update(UserUpdate dto) {
        Map<UUID, User> userData = userRepository.getUserData();

        User user = Optional.ofNullable(userData.get(dto.getUserID()))
                .orElseThrow(() -> new NoSuchElementException("User with id " + dto.getUserID() + " not found"));

        return userRepository.update(user, dto.getNewUserName(), dto.getNewEmail(), dto.getNewPassword(), dto.getNewProfileID());
    }

    @Override
    public void delete(UUID userId) {
        Map<UUID, User> userData = userRepository.getUserData();
        if (!userData.containsKey(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        userRepository.delete(userId);
        userStatusRepository.delete(userStatusRepository.findByUserId(userId).getId());
        binaryContentRepository.delete(binaryContentRepository.getBinaryContentByUserId(userId).getId());
    }
}
