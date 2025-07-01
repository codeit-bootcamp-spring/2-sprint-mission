package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.role.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserMapper userMapper;

    private final BinaryContentStorage binaryContentStorage;
    private final PasswordEncoder passwordEncoder;

    private final SessionManager sessionManager;

    private final UserOnlineSessionService userOnlineSessionService;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
        Optional<BinaryContentCreateRequest> profileCreateRequest) {

        log.info("Creating user");

        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        if (userRepository.existsByEmail(email)) {
            log.warn("Email {} already exists", email);
            throw UserAlreadyExistsException.withEmail(email);
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("Username {} already exists", username);
            throw UserAlreadyExistsException.withUsername(username);
        }

        BinaryContent profile = profileCreateRequest
            .map(request -> {
                String fileName = request.fileName();
                String contentType = request.contentType();
                byte[] bytes = request.bytes();
                log.debug("Profile image bytes processed: size = {} bytes", bytes.length);
                BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                    contentType);
                binaryContentRepository.save(binaryContent);
                binaryContentStorage.put(binaryContent.getId(), bytes);
                return binaryContent;
            })
            .orElse(null);
        String password = passwordEncoder.encode(userCreateRequest.password());

        User user = new User(username, email, password, profile);
        userRepository.save(user);

        log.info("User created successfully: username = {}", username);

        return userMapper.toDto(user).withOnline(userOnlineSessionService.isOnline(user.getId()));
    }

    @Override
    public UserDto searchUser(UUID userId) {
        User user = getUser(userId);
        return userMapper.toDto(user).withOnline(userOnlineSessionService.isOnline(user.getId()));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
            .stream()
            .map(user -> userMapper.toDto(user)
                .withOnline(userOnlineSessionService.isOnline(user.getId())))
            .toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> profileCreateRequest) {

        log.info("Updating user : id = {}", userId);

        User user = getUser(userId);

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();

        if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
            log.warn("Email {} already exists", newEmail);
            throw UserAlreadyExistsException.withEmail(newEmail);
        }
        if (!newUsername.equals(user.getUsername()) && userRepository.existsByUsername(
            newUsername)) {
            log.warn("Username {} already exists", newUsername);
            throw UserAlreadyExistsException.withUsername(newUsername);
        }

        BinaryContent profile = profileCreateRequest
            .map(request -> {
                String fileName = request.fileName();
                String contentType = request.contentType();
                byte[] bytes = request.bytes();
                log.debug("Updated profile image bytes: size = {} bytes", bytes.length);
                BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                    contentType);
                binaryContentRepository.save(binaryContent);
                binaryContentStorage.put(binaryContent.getId(), bytes);
                return binaryContent;
            })
            .orElse(null);
        String newPassword = userUpdateRequest.newPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            newPassword = passwordEncoder.encode(newPassword);
        }
        user.updateUser(newUsername, newEmail, newPassword, profile);

        log.info("User updated successfully: username = {}", newUsername);

        return userMapper.toDto(user).withOnline(userOnlineSessionService.isOnline(user.getId()));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    @Override
    public UserDto updateRole(RoleUpdateRequest request) {
        User user = getUser(request.userId());
        user.updateRole(request.newRole());
        sessionManager.invalidateSession(user.getId());
        return userMapper.toDto(user).withOnline(userOnlineSessionService.isOnline(user.getId()));
    }

    @Transactional
    @Override
    public void delete(UUID userId) {

        log.info("Deleting user : id = {}", userId);

        if (!userRepository.existsById(userId)) {
            throw UserNotFoundException.withId(userId);
        }
        userRepository.deleteById(userId);

        log.info("User deleted successfully : id = {}", userId);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("User with id {} not found", userId);
                return UserNotFoundException.withId(userId);
            });
    }


}
