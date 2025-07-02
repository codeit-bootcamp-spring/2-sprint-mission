package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.dto.file.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.DuplicateUserException;
import com.sprint.mission.discodeit.exception.user.ProfileUploadFailedException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentService binaryContentService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest request,
        Optional<CreateBinaryContentRequest> profileOpt) {

        log.info("사용자 생성 요청 - username: {}, email: {}", request.username(), request.email());

        if (userRepository.findAll().stream().anyMatch(
            user -> user.getUsername().equals(request.username()) || user.getEmail()
                .equals(request.email())
        )) {
            log.warn("이미 존재하는 사용자 - username: {}, email: {}", request.username(), request.email());
            throw new DuplicateUserException(
                Map.of("username", request.username(), "email", request.email()));
        }

        BinaryContent profile = profileOpt.map(p -> {
            log.debug("프로필 이미지 저장 시작");
            BinaryContentDto dto = binaryContentService.create(p);
            return binaryContentRepository.findById(dto.id())
                .orElseThrow(() -> new ProfileUploadFailedException(Map.of("파일이름", p.fileName())));
        }).orElse(null);

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(
            request.username(),
            encodedPassword,
            request.email(),
            profile
        );
        userRepository.save(user);

        log.info("사용자 생성 완료 - userId: {}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(UUID userId) {
        return userRepository.findById(userId)
            .map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByName(String name) {
        return userRepository.findAll().stream()
            .filter(user -> user.getUsername().equals(name))
            .map(userMapper::toDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(userMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public UserDto updateUser(UpdateUserRequest request,
        Optional<CreateBinaryContentRequest> profileOpt) {

        UUID userId = request.userId();
        log.info("사용자 수정 요청 - userId: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        String newUsername = request.newUsername();
        String newEmail = request.newEmail();

        if (newUsername != null &&
            !newUsername.equals(user.getUsername()) &&
            userRepository.existsByUsername(newUsername)) {
            throw new DuplicateUserException(Map.of("username", newUsername));
        }

        if (newEmail != null &&
            !newEmail.equals(user.getEmail()) &&
            userRepository.existsByEmail(newEmail)) {
            throw new DuplicateUserException(Map.of("email", newEmail));
        }

        BinaryContent newProfile = profileOpt.map(profile -> {
            log.debug("기존 프로필 삭제 및 새로운 프로필 저장");

            Optional.ofNullable(user.getProfile())
                .ifPresent(binaryContentRepository::delete);

            BinaryContentDto dto = binaryContentService.create(profile);
            return binaryContentRepository.findById(dto.id())
                .orElseThrow(
                    () -> new ProfileUploadFailedException(Map.of("파일이름", profile.fileName())));
        }).orElse(user.getProfile());

        user.update(newUsername, request.newPassword(), newEmail, newProfile);

        log.info("사용자 수정 완료 - userId: {}", userId);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {

        log.info("사용자 삭제 요청 - userId: {}", userId);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        if (user.getProfile() != null) {
            binaryContentRepository.delete(user.getProfile());
            log.debug("사용자 프로필 삭제 완료 - userId: {}", userId);
        }
        userRepository.delete(user);
        log.info("사용자 삭제 완료 - userId: {}", userId);

    }

    @Override
    public UserDto updateUserRole(UUID userId, Role newRole) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        user.setRole(newRole);
        User updatedUser = userRepository.save(user);

        return userMapper.toDto(updatedUser);
    }
}
