package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
                          BinaryContentCreateRequest profileCreateRequest) {
        String userName = userCreateRequest.username();
        String email = userCreateRequest.email();

        if (userRepository.existsByUsername(userName)) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }

        BinaryContent profile = toBinaryContent(Optional.ofNullable(profileCreateRequest));

        String password = userCreateRequest.password();

        User user = new User(userName, email, password, profile);
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user, Instant.now());
        userStatusRepository.save(userStatus);

        return toDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto searchUser(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public User searchByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()->new NoSuchElementException("User with username " + username + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    @Override
    public User update(UUID userId, UserUpdateRequest userUpdateRequest, BinaryContentCreateRequest profileCreateRequest) {
        User user = getUser(userId);

        String newUserName = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("User with email " + newEmail + " already exists");
        }
        if (!newUserName.equals(user.getUsername()) && userRepository.existsByUsername(newUserName)) {
            throw new IllegalArgumentException("User with username " + newUserName + " already exists");
        }

        Optional.ofNullable(user.getProfile())
                .ifPresent(binaryContentRepository::delete);

        BinaryContent profile = toBinaryContent(Optional.ofNullable(profileCreateRequest));

        String newPassword = userUpdateRequest.newPassword();
        user.updateUser(newUserName, newEmail, newPassword, profile);

        return user;
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        User user = getUser(userId);
        Optional.ofNullable(user.getProfile())
                .ifPresent(binaryContentRepository::delete);

        userStatusRepository.deleteByUser_Id(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto updateOnlineState(UUID userId) {
        User user = getUser(userId);
        return toDto(user);
    }

    private UserDto toDto(User user) {
        Boolean online = userStatusRepository.findByUser_Id(user.getId())
                .map(UserStatus::isOnline)
                .orElse(false);

        BinaryContentDto profileDto = Optional.ofNullable(user.getProfile())
                .map(profile -> new BinaryContentDto(
                        profile.getId(),
                        profile.getFileName(),
                        profile.getSize(),
                        profile.getContentType()
                )).orElse(null);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profileDto,
                online
        );
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    private BinaryContent toBinaryContent(Optional<BinaryContentCreateRequest> profileCreateRequest) {
        return profileCreateRequest
                .map(request -> new BinaryContent(
                        request.fileName(),
                        (long) request.bytes().length,
                        request.contentType(),
                        request.bytes()
                )).map(binaryContentRepository::save)
                .orElse(null);
    }

}
