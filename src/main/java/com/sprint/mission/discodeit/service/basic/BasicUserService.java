package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public UserDto create(
        UserCreateRequest userCreateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest
    ) {
        log.info("User Create 서비스 시작, User name: {}", userCreateRequest.username());
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        // 이미 존재할 경우 예외처리
        if (userRepository.existsByUsername(username)) {
            log.warn("사용자 이름 중복: {}", username);
            // 커스텀 예외처리
            throw new UserAlreadyExistsException(username);
        }
        if (userRepository.existsByEmail(email)) {
            log.warn("사용자 이메일 중복: {}", email);
            throw new UserAlreadyExistsException(email);
        }

        // binaryContent가 입력됐다면 실행
        BinaryContent nullableProfile = optionalProfileCreateRequest
            .map(profileRequest -> {
                try {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    log.info("BinaryContent 프로필 파일 저장 시작");
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                        contentType);
                    binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(binaryContent.getId(), bytes);
                    log.info("BinaryContent 저장 완료");
                    return binaryContent;
                } catch (Exception e) {
                    log.error("프로필 파일 저장 중 오류 발생", e);
                    throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR,
                        Map.of("프로필 파일 저장 실패", e));
                }
            })
            .orElse(null);
        String password = userCreateRequest.password();

        User user = new User(username, email, password, nullableProfile);
        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(user, now);

        userRepository.save(user);
        log.info("사용자 생성 성공");
        return userMapper.toDto(user);
    }

    @Override
    public UserDto find(UUID userId) {
        log.info("사용자 Id로 정보 탐색 서비스");
        return userRepository.findById(userId)
            .map(userMapper::toDto)
            .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<UserDto> findAll() {
        log.info("전체 사용자 정보 탐색 서비스");
        return userRepository.findAllWithProfileAndStatus()
            .stream()
            .map(userMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public UserDto update(
        UUID userId,
        UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest
    ) {
        log.debug("사용자 정보 업데이트 서비스");
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();
        if (userRepository.existsByEmail(newEmail)) {
            log.warn("사용자 이메일 중복, email: {}", newEmail);
            // 커스텀 예외처리
            throw new UserAlreadyExistsException(newEmail);
        }
        if (userRepository.existsByUsername(newUsername)) {
            log.warn("사용자 이름 중복, userName: {}", newUsername);
            // 커스텀 예외처리
            throw new UserAlreadyExistsException(newUsername);
        }

        BinaryContent nullableProfile = optionalProfileCreateRequest
            .map(profileRequest -> {
                try {
                    log.info("프로필 파일 업데이트 시작");
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
                        contentType);
                    binaryContentRepository.save(binaryContent);
                    binaryContentStorage.put(binaryContent.getId(), bytes);
                    log.info("프로필 파일 저장 성공");
                    return binaryContent;
                } catch (Exception e) {
                    log.error("프로필 파일 업데이트 실패");
                    throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR,
                        Map.of("프로필 파일 업데이트 실패", e));
                }
            })
            .orElse(null);

        String newPassword = userUpdateRequest.newPassword();
        user.update(newUsername, newEmail, newPassword, nullableProfile);

        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        if (userRepository.existsById(userId)) {
            log.warn("사용자 정보 없음");
            throw new UserNotFoundException(userId);
        }
        log.info("사용자 정보 삭제 요청 서비스");
        userRepository.deleteById(userId);
        log.info("사용자 정보 삭제 성공");
    }
}
