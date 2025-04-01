package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
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
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserCreateRequestDto dto) {
        if (userRepository.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(dto.getUsername()))) {
            throw new IllegalArgumentException("같은 이름을 가진 사람이 있습니다.");
        }
        if (userRepository.findAll().stream()
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
    public UserResponseDto find(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        User user = userRepository.findById(userId);

        return new UserResponseDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getUsername(),
                user.getEmail(), user.getProfileId(), userStatus.isOnline());
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(
                        user -> {
                            UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
                            return new UserResponseDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
                                    user.getUsername(),
                                    user.getEmail(), user.getProfileId(), userStatus.isOnline());
                        }).toList();
    }

    @Override
    public User update(UserUpdateRequestDto dto) {
        User user = Optional.ofNullable(userRepository.findById(dto.getUserID()))
                .orElseThrow(() -> new NoSuchElementException("User with id " + dto.getUserID() + " not found"));

        return userRepository.update(user, dto.getNewUserName(), dto.getNewEmail(), dto.getNewPassword(), dto.getNewProfileID());
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId);

        userRepository.delete(userId);
        userStatusRepository.delete(userStatusRepository.findByUserId(userId).getId());

        if (user.getProfileId() != null) {
            BinaryContent binaryContent = binaryContentRepository.findById(user.getProfileId());
            binaryContentRepository.delete(binaryContent.getId());
        }
    }
}
