package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
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

    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
                          BinaryContentCreateRequest profileCreateRequest) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }

        BinaryContent profile = toBinaryContent(profileCreateRequest);

        User user = new User(username, email, userCreateRequest.password(), profile);
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user, Instant.now());
        userStatusRepository.save(userStatus);

        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto searchUser(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public User searchByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User with username " + username + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, BinaryContentCreateRequest profileCreateRequest) {
        User user = getUser(userId);

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();

        if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("User with email " + newEmail + " already exists");
        }
        if (!newUsername.equals(user.getUsername()) && userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("User with username " + newUsername + " already exists");
        }

        Optional.ofNullable(user.getProfile())
                .ifPresent(binaryContentRepository::delete);

        BinaryContent profile = toBinaryContent(profileCreateRequest);

        user.updateUser(newUsername, newEmail, userUpdateRequest.newPassword(), profile);
        return userMapper.toDto(user);
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

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    private BinaryContent toBinaryContent(BinaryContentCreateRequest profileCreateRequest) {
        if (profileCreateRequest == null) {
            return null;
        }
        BinaryContent binaryContent = new BinaryContent(
                profileCreateRequest.fileName(),
                (long) profileCreateRequest.bytes().length,
                profileCreateRequest.contentType(),
                profileCreateRequest.bytes()
        );
        return binaryContentRepository.save(binaryContent);
    }

}
