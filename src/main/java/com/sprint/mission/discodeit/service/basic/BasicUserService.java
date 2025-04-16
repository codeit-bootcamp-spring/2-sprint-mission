package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentStorage;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Transactional
    @Override
    public User create(UserCreateRequest userCreateRequest,

        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

        String username = userCreateRequest.username();
        String email = userCreateRequest.email();
        String password = userCreateRequest.password();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                "User with username " + username + " already exists");
        }

        BinaryContent nullableProfileObject = optionalProfileCreateRequest.map(profileRequest -> {
            String contentType = profileRequest.contentType();
            String fileName = profileRequest.fileName();
            byte[] bytes = profileRequest.bytes();

            BinaryContent binaryContent = new BinaryContent(
                contentType,
                fileName,
                (long) bytes.length
            );
            binaryContentStorage.put(binaryContent.getId(), bytes);
            return binaryContentRepository.save(binaryContent);
        }).orElse(null);

        User user = new User(
            username,
            email,
            password,
            nullableProfileObject
        );
        User createdUser = userRepository.save(user);
        //userstatue 생성
        UserStatus userStatus = new UserStatus(createdUser);
        userStatusRepository.save(userStatus);

        return createdUser;
    }


    @Override
    public UserDto find(UUID userId) {
        return userRepository.findById(userId)
            .map(this::toDto)
            .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(user -> new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            binaryContentMapper.toDto(user.getProfile()),
            user.getUserStatus().isOnline()
        ));


    }

    @Override
    public User update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("User with email " + newEmail + " already exists");
        }
        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException(
                "User with username " + newUsername + " already exists");
        }

        UUID nullableProfileId = optionalProfileCreateRequest
            .map(profileRequest -> {
                Optional.ofNullable(user.getProfileId())
                    .ifPresent(binaryContentRepository::deleteById);

                String fileName = profileRequest.fileName();
                String contentType = profileRequest.contentType();
                byte[] bytes = profileRequest.bytes();
                BinaryContent binaryContent = new BinaryContent(contentType, fileName,
                    (long) bytes.length);
                return binaryContentRepository.save(binaryContent).getId();
            })
            .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        user.update(newUsername, newEmail, newPassword, nullableProfileId);

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        Optional.ofNullable(user.getProfileId())
            .ifPresent(binaryContentRepository::deleteById);
        userStatusRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }

    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
            .map(UserStatus::isOnline)
            .orElse(null);

        return new UserDto(
            user.getId(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getUsername(),
            user.getEmail(),
            user.getProfileId(),
            online
        );
    }
}
