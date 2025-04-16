package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.PageResponse;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentStorage;
import com.sprint.mission.discodeit.service.UserService;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(BasicUserService.class);

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository; // 메타데이터 관리
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentStorage binaryContentStorage;// 실제 데이터 관리
    private final UserMapper userMapper;
    private final PageResponseMapper pageResponseMapper;

    @Transactional // 데이터 변경 작업
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

        String username = userCreateRequest.username();
        String email = userCreateRequest.email();
        String password = userCreateRequest.password();

        log.info("사용자 생성 시작: email={}", email);
        validateUserDoesNotExist(username, email);

        // 프로필 존재 시
        BinaryContent profile = null;
        if (optionalProfileCreateRequest.isPresent()) {
            BinaryContentCreateRequest profileRequest = optionalProfileCreateRequest.get();
            byte[] bytes = profileRequest.bytes();

            if (bytes != null && bytes.length > 0) {
                profile = new BinaryContent(
                    profileRequest.contentType(),
                    profileRequest.fileName(),
                    (long) bytes.length
                );
                UUID profileId = profile.getId();

                try {
                    binaryContentStorage.put(profileId, bytes);
                    binaryContentRepository.save(profile);
                } catch (Exception e) {
                    throw new RuntimeException("프로필 이미지 저장 중 오류 발생", e);
                }
            }
        }

        User user = new User(
            username,
            email,
            password,
            profile
        );
        User createdUser = userRepository.save(user);
        // 유저 스테이터스 생성
        UserStatus userStatus = new UserStatus(createdUser);
        userStatusRepository.save(userStatus);

        return userMapper.toDto(createdUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto find(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserDto> findAll(Pageable pageable) {
        //page로 받은 후 -> 다시 변환
        Page<User> userPage = userRepository.findAll(pageable);
        PageResponse<UserDto> response = pageResponseMapper.fromPage(userPage, userMapper::toDto);
        return response;
    }


    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

        log.info("사용자 업데이트 시작: ID={}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        if (!user.getUsername().equals(newUsername) && userRepository.existsByUsername(
            newUsername)) {
            throw new IllegalArgumentException("Username '" + newUsername + "' is already taken");
        }
        if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email '" + newEmail + "' is already taken");
        }

        BinaryContent newProfileEntity = null;
        boolean profileChanged = optionalProfileCreateRequest.isPresent();
        user.setProfile(null);

        //  새 프로필 처리
        BinaryContentCreateRequest profileRequest = optionalProfileCreateRequest.get();
        byte[] bytes = profileRequest.bytes();
        if (bytes != null && bytes.length > 0) {

            newProfileEntity = new BinaryContent(
                profileRequest.contentType(),
                profileRequest.fileName(),
                (long) bytes.length
            );
            UUID newProfileId = newProfileEntity.getId();

            try {
                binaryContentStorage.put(newProfileId, bytes);
                user.setProfile(newProfileEntity);
            } catch (Exception e) {
                throw new RuntimeException("새 프로필 이미지 저장 중 오류 발생" + e.getMessage());
            }
        }

        user.setUsername(newUsername);
        user.setEmail(newEmail);
        if (userUpdateRequest.newPassword() != null && !userUpdateRequest.newPassword().isEmpty()) {
            user.setPassword(userUpdateRequest.newPassword());
        }

        User updatedUser = userRepository.save(user);
        log.info("사용자 업데이트 완료: ID={}", userId);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        log.info("사용자 삭제 시작: ID={}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        userRepository.delete(user);
    }


    private void validateUserDoesNotExist(String username, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                "User with username " + username + " already exists");
        }
    }


}