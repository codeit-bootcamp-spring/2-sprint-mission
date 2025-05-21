package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.Mapper.UserMapper;
import com.sprint.mission.discodeit.Mapper.UserStatusMapper;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateUserEmail;
import com.sprint.mission.discodeit.exception.user.DuplicateUserUserName;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public UserDto create(UserCreateRequest userCreateRequest,
                          Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();
        log.info("사용자 생성 요청: username={}, email={}, hasProfile={}",
                username, email, optionalProfileCreateRequest.isPresent());

        if (userRepository.existsByEmail(email)) {
            log.warn("사용자 생성 실패 (이메일 중복): email={}", email);
            throw new DuplicateUserEmail(Map.of("email", email));
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("사용자 생성 실패 (username 중복): username={}", username);
            throw new DuplicateUserUserName(Map.of("username", username));
        }

        BinaryContent profile = optionalProfileCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    Long size = (long) profileRequest.bytes().length;
                    String contentType = profileRequest.contentType();
                    log.info("프로파일 저장 요청: fileName={}, size={}, contentType={}",
                            fileName, size, contentType);

                    BinaryContent bc = binaryContentRepository.save(
                            new BinaryContent(fileName, size, contentType));
                    UUID uuid = binaryContentStorage.put(bc.getId(), profileRequest.bytes());
                    log.info("프로파일 저장 완료: id={}, uuid={}", bc.getId(), uuid);
                    return bc;
                })
                .orElse(null);

        String password = userCreateRequest.password();
        User user = new User(username, email, password, profile);
        User created = userRepository.save(user);
        log.info("사용자 저장 완료: id={}, username={}", created.getId(), created.getUsername());

        Instant now = Instant.now();
        UserStatus status = new UserStatus(created, now);
        userStatusRepository.save(status);
        log.info("UserStatus 생성 완료: userId={}, timestamp={}", created.getId(), now);

        UserDto dto = userMapper.toDto(created);
        log.info("사용자 생성 완료: dto={}", dto);
        return dto;
    }

    @Override
    public UserDto find(UUID userId) {
        log.info("사용자 조회 요청: id={}", userId);
        UserDto dto = userRepository.findById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("사용자 조회 실패: id={} not found", userId);
                    return new UserNotFoundException(Map.of("id", userId));
                });
        log.info("사용자 조회 완료: id={}", userId);
        return dto;
    }

    @Override
    public List<UserDto> findAll() {
        log.info("전체 사용자 조회 요청");
        List<UserDto> list = userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
        log.info("전체 사용자 조회 완료: count={}", list.size());
        return list;
    }

    @Override
    @Transactional
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
                          Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        log.info("사용자 수정 요청: id={}, newUsername={}, newEmail={}, hasProfile={}",
                userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail(),
                optionalProfileCreateRequest.isPresent());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("사용자 수정 실패: id={} not found", userId);
                    return new UserNotFoundException(Map.of("id", userId));
                });

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        if (userRepository.existsByEmail(newEmail)) {
            log.warn("사용자 수정 실패 (이메일 중복): newEmail={}", newEmail);
            throw new DuplicateUserEmail(Map.of("email", newEmail));
        }
        if (userRepository.existsByUsername(newUsername)) {
            log.warn("사용자 수정 실패 (username 중복): newUsername={}", newUsername);
            throw new DuplicateUserUserName(Map.of("userName", newUsername));
        }

        BinaryContent profile = optionalProfileCreateRequest
                .map(profileRequest -> {
                    Optional.ofNullable(user.getProfile())
                            .ifPresent(p -> {
                                binaryContentRepository.deleteById(p.getId());
                                log.info("기존 프로파일 삭제: id={}", p.getId());
                            });
                    String fileName = profileRequest.fileName();
                    Long size = (long) profileRequest.bytes().length;
                    String contentType = profileRequest.contentType();
                    BinaryContent bc = binaryContentRepository.save(
                            new BinaryContent(fileName, size, contentType));
                    UUID uuid = binaryContentStorage.put(bc.getId(), profileRequest.bytes());
                    log.info("새 프로파일 저장 완료: id={}, uuid={}", bc.getId(), uuid);
                    return bc;
                })
                .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        user.update(newUsername, newEmail, newPassword, profile);
        User updated = userRepository.save(user);
        log.info("사용자 수정 완료: id={}, username={}", updated.getId(), updated.getUsername());

        UserDto dto = userMapper.toDto(updated);
        log.info("수정된 사용자 DTO: {}", dto);
        return dto;
    }

    @Override
    @Transactional
    public void delete(UUID userId) {
        log.info("사용자 삭제 요청: id={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("사용자 삭제 실패: id={} not found", userId);
                    return new UserNotFoundException(Map.of("id", userId));
                });

        Optional.ofNullable(user.getProfile())
                .ifPresent(p -> {
                    binaryContentRepository.deleteById(p.getId());
                    log.info("프로파일 삭제 완료: id={}", p.getId());
                });
        userRepository.deleteById(userId);
        log.info("사용자 삭제 완료: id={}", userId);
    }
}