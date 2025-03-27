package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.CreateUserDTO;
import com.sprint.mission.discodeit.dto.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.user.UserResponseDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User createUser(CreateUserDTO dto, Optional<BinaryContentDTO> profileCreateRequest) {
        String userName = dto.userName();
        String email = dto.email();
        if (userRepository.existsByUsername(userName)) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }

        UUID nullableProfileId = profileCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        String password = dto.password();

        User user = new User(userName, email, password, nullableProfileId);
        User newUser = userRepository.save(user);

        UserStatus userStatus = new UserStatus(newUser.getId(), Instant.now());
        userStatusRepository.saveUserStatus(userStatus);

        return user;
    }

    @Override
    public UserResponseDTO searchUser(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<UserResponseDTO> searchAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public User updateUser(UUID userId, UpdateUserDTO dto, Optional<BinaryContentDTO> profileCreateRequest) {
        User user = getUser(userId);

        String newUserName = dto.userName();
        String newEmail = dto.email();
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("User with email " + newEmail + " already exists");
        }
        if (userRepository.existsByUsername(newUserName)) {
            throw new IllegalArgumentException("User with username " + newUserName + " already exists");
        }
        UUID profileId = profileCreateRequest
                .map(profileRequest -> {
                    Optional.ofNullable(user.getProfileId())
                            .ifPresent(binaryContentRepository::deleteById);

                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        String newPassword = dto.password();
        user.updateUser(newUserName, newEmail, newPassword, profileId);

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = getUser(userId);
        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);

        userStatusRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponseDTO updateOnlineState(UUID userId) {
        User user = getUser(userId);
        return toDto(user);
    }

    private UserResponseDTO toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(UserStatus::isOnline)
                .orElse(false);

        return new UserResponseDTO(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + userId + "인 사용자를 찾을 수 없습니다."));
    }

}
