package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.async.BinaryContentAsyncService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserMapper userMapper;

    private final BinaryContentStorage binaryContentStorage;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final BinaryContentAsyncService binaryContentAsyncService;

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
                binaryContent.setUploadStatus(BinaryContentUploadStatus.WAITING);
                binaryContentRepository.save(binaryContent);

                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            binaryContentAsyncService.uploadFile(binaryContent.getId(), bytes);
                        }
                    }
                );
                return binaryContent;
            })
            .orElse(null);
        String password = passwordEncoder.encode(userCreateRequest.password());

        User user = new User(username, email, password, profile);
        userRepository.save(user);

        log.info("User created successfully: username = {}", username);

        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto find(UUID userId) {
        log.debug("사용자 조회 시작: id = {}", userId);
        UserDto userDto = userRepository.findById(userId)
            .map(userMapper::toDto)
            .orElseThrow(() -> UserNotFoundException.withId(userId));
        log.info("사용자 조회 완료: id={}", userId);
        return userDto;
    }

    @Override
    public List<UserDto> findAll() {
        log.debug("모든 사용자 조회 시작");
        Set<UUID> onlineUserIds = jwtService.getActiveJwtSessions().stream()
            .map(JwtSession::getUserId)
            .collect(Collectors.toSet());

        List<UserDto> userDtos = userRepository.findAllWithProfile()
            .stream()
            .map(user -> userMapper.toDto(user, onlineUserIds.contains(user.getId())))
            .toList();
        log.info("모든 사용자 조회 완료: 총 {}명", userDtos.size());
        return userDtos;
    }

    @PreAuthorize("hasPermission(#userId,'User','update')")
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
                binaryContent.setUploadStatus(BinaryContentUploadStatus.WAITING);
                binaryContentRepository.save(binaryContent);

                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            binaryContentAsyncService.uploadFile(binaryContent.getId(), bytes);
                        }
                    }
                );
                return binaryContent;
            })
            .orElse(null);
        String newPassword = userUpdateRequest.newPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            newPassword = passwordEncoder.encode(newPassword);
        }
        user.updateUser(newUsername, newEmail, newPassword, profile);

        log.info("User updated successfully: username = {}", newUsername);

        return userMapper.toDto(user);
    }

    @PreAuthorize("hasPermission(#userId,'User','delete')")
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
