package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public UserDto save(UserCreateRequest userCreateRequest, MultipartFile profile)
        throws IOException {
        log.info("사용자 생성 진행: username = {}, email = {}", userCreateRequest.username(),
            userCreateRequest.email());

        if (userRepository.findByUsername(userCreateRequest.username()).isPresent()) {
            log.warn("사용자 생성 중 이미 존재하는 사용자 이름 발견: username = {}", userCreateRequest.username());
            throw new IllegalArgumentException(
                String.format("User with username %s already exists",
                    userCreateRequest.username()));
        }

        if (userRepository.findByEmail(userCreateRequest.email()).isPresent()) {
            log.warn("사용자 생성 중 이미 존재하는 이메일: email = {}", userCreateRequest.email());
            throw new IllegalArgumentException(
                String.format("User with email %s already exists", userCreateRequest.email()));
        }

        BinaryContent binaryContent = null;
        if (profile != null && !profile.isEmpty()) {
            log.debug("사용자 생성 중 프로필 이미지 메타데이터 저장: filename = {}, contentType = {}, size = {}",
                profile.getOriginalFilename(), profile.getContentType(), profile.getSize());
            binaryContent = BinaryContent.builder()
                .fileName(profile.getOriginalFilename())
                .contentType(profile.getContentType())
                .size((long) profile.getBytes().length)
                .build();
        }

        User user = new User(
            userCreateRequest.username(),
            userCreateRequest.password(),
            userCreateRequest.email(),
            binaryContent);

        userRepository.save(user);

        if (binaryContent != null) {
            log.debug("사용자 생성 중 프로필 저장: profileId = {}", binaryContent.getId());
            binaryContentStorage.put(binaryContent.getId(), profile.getBytes());
        }

        log.info("사용자 생성 완료: userId = {}", user.getId());
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAllUser() {
        return userRepository.findAllWithProfileAndStatus().stream()
            .map(userMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        MultipartFile profile) throws IOException {
        log.info("사용자 수정 진행: userId={}", userId);

        User user = userRepository.findById(userId).
            orElseThrow(() -> {
                log.error("사용자 수정 중 대상 사용자를 찾을 수 없음: userId={}", userId);
                return new NoSuchElementException(
                    String.format("User with id %s not found", userId));
            });

        if (userRepository.findByUsername(userUpdateRequest.newUsername())
            .filter(otherUser -> !otherUser.getId().equals(user.getId()))
            .isPresent()) {
            log.warn("사용자 수정중 중복된 사용자 이름 발견: newUsername = {}", userUpdateRequest.newUsername());
            throw new IllegalArgumentException(
                String.format("User with username %s already exists",
                    userUpdateRequest.newUsername()));
        }

        if (userRepository.findByEmail(userUpdateRequest.newEmail())
            .filter(otherUser -> !otherUser.getId().equals(user.getId()))
            .isPresent()) {
            log.warn("사용자 수정중 중복된 이메일 발견: newEmail = {}", userUpdateRequest.newEmail());
            throw new IllegalArgumentException(
                String.format("User with email %s already exists", userUpdateRequest.newEmail()));
        }

        BinaryContent binaryContent = null;
        if (profile != null && !profile.isEmpty()) {
            log.debug("사용자 수정 중 프로필 이미지 메타데이터 저장: filename = {}, contentType = {}, size = {}",
                profile.getOriginalFilename(), profile.getContentType(), profile.getSize());
            binaryContent = BinaryContent.builder()
                .fileName(profile.getOriginalFilename())
                .contentType(profile.getContentType())
                .size((long) profile.getBytes().length)
                .build();

            user.updateProfile(binaryContent);
        }

        user.updateUsername(userUpdateRequest.newUsername());
        user.updatePassword(userUpdateRequest.newPassword());
        user.updateEmail(userUpdateRequest.newEmail());

        User updatedUser = userRepository.saveAndFlush(user);

        log.info("사용자 수정 완료: userId = {}", user.getId());

        if (updatedUser.getProfile() != null) {
            System.out.println(updatedUser.getProfile().getId());
            log.debug("프로필 이미지 파일 저장: profileId = {}", updatedUser.getProfile().getId());
            binaryContentStorage.put(updatedUser.getProfile().getId(), profile.getBytes());
        }

        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.error("사용자 삭제 중 사용자를 찾을 수 없음: userId = {}", userId);
                return new NoSuchElementException(
                    String.format("User with id %s not found", userId));
            });
        log.info("사용자 삭제 완료: userId = {}", userId);
        userRepository.delete(user);
    }
}
