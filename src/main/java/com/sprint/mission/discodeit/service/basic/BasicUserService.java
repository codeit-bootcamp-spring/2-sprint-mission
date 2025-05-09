package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserOperationRestrictedException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentStorage;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final LocalBinaryContentStorage localBinaryContentStorage;
    private final MessageRepository messageRepository;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        log.info("사용자 생성 시작. 사용자명: {}", userCreateRequest.username());

        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        validateUserDoesNotExist(username, email);

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
                    log.info("프로필 이미지 저장 완료. 프로필 ID: {}", profileId);
                } catch (Exception e) {
                    log.error("사용자 '{}'의 프로필 이미지 저장 중 오류 발생", username, e);
                    throw new FileProcessingCustomException("save-profile",
                        profileRequest != null ? profileRequest.fileName() : "unknown_file",
                        "사용자 " + username + "의 프로필 이미지 저장 중 오류가 발생했습니다.");
                }
            }
        }

        User user = new User(
            username,
            email,
            userCreateRequest.password(),
            profile
        );
        User createdUser = userRepository.save(user);
        log.info("사용자 정보 DB 저장 완료. ID: {}", createdUser.getId());

        UserStatus userStatus = new UserStatus(createdUser);
        userStatusRepository.save(userStatus);
        createdUser.setUserStatus(userStatus);
        log.info("사용자 상태 정보 생성 및 연결 완료. 사용자 ID: {}", createdUser.getId());

        UserDto userDto = userMapper.toDto(createdUser);
        log.info("사용자 생성 완료. 반환된 사용자 ID: {}", userDto.id());
        return userDto;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto find(UUID userId) {
        log.info("사용자 조회 시도. ID: {}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId.toString()));
        log.info("사용자 조회 성공. ID: {}", userId);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        log.info("모든 사용자 조회 시도.");
        List<User> users = userRepository.findAll();
        log.info("총 {}명의 사용자를 조회했습니다.", users.size());
        return userMapper.toDto(users);
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
        log.info("사용자 업데이트 시작. ID: {}", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if ("admin".equalsIgnoreCase(user.getUsername())) {
            log.warn("Attempt to update restricted user: {}", userId);
            throw new UserOperationRestrictedException(userId.toString(), "update",
                "관리자 계정(" + user.getUsername() + ")은 수정할 수 없습니다.");
        }

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();

        if (newUsername != null && !newUsername.isEmpty() && !user.getUsername()
            .equals(newUsername)) {
            if (userRepository.existsByUsername(newUsername)) {
                log.warn("사용자 업데이트 실패 (사용자명 중복). ID: {}, 시도한 사용자명: '{}'", userId, newUsername);
                throw new UserAlreadyExistException(ErrorCode.USER_ALREADY_EXISTS,
                    Map.of("username", newUsername));
            }
            user.setUsername(newUsername);
            log.info("사용자 ID '{}'의 사용자명을 '{}'(으)로 변경.", userId, newUsername);
        }

        if (newEmail != null && !newEmail.isEmpty() && !user.getEmail().equals(newEmail)) {
            if (userRepository.existsByEmail(newEmail)) {
                log.warn("사용자 업데이트 실패 (이메일 중복). ID: {}, 시도한 이메일: '{}'", userId, newEmail);
                throw new UserAlreadyExistException(ErrorCode.USER_ALREADY_EXISTS,
                    Map.of("email", newEmail));
            }
            user.setEmail(newEmail);
            log.info("사용자 ID '{}'의 이메일을 '{}'(으)로 변경.", userId, newEmail);
        }

        if (userUpdateRequest.newPassword() != null && !userUpdateRequest.newPassword().isEmpty()) {
            user.setPassword(userUpdateRequest.newPassword());
            log.info("사용자 ID '{}'의 비밀번호를 변경했습니다.", userId);
        }

        UUID oldProfileId = null;
        if (user.getProfile() != null) {
            oldProfileId = user.getProfile().getId();
            log.info("사용자 ID '{}'의 기존 프로필 참조를 제거합니다. (프로필 ID: {}) orphanRemoval에 의해 삭제 예정", userId,
                oldProfileId);
            user.setProfile(null);
        }

        if (optionalProfileCreateRequest.isPresent()) {
            BinaryContentCreateRequest profileRequest = optionalProfileCreateRequest.get();
            byte[] bytes = profileRequest.bytes();
            if (bytes != null && bytes.length > 0) {
                BinaryContent newProfileEntity = new BinaryContent(
                    profileRequest.contentType(),
                    profileRequest.fileName(),
                    (long) bytes.length
                );
                user.setProfile(newProfileEntity);
                try {
                    log.info("새 프로필 이미지 정보를 설정했습니다. 파일명: '{}'. 실제 저장은 사용자 저장 후 진행.",
                        profileRequest.fileName());
                } catch (Exception e) {
                    log.error("사용자 ID '{}'의 새 프로필 이미지 설정 중 오류 발생 (파일 저장 전)", userId, e);
                    throw new RuntimeException("새 프로필 이미지 설정 중 오류 발생: " + e.getMessage());
                }
            }
        }

        User updatedUser = userRepository.save(user);
        log.info("사용자 업데이트 DB 저장 완료. ID: {}", userId);

        if (updatedUser.getProfile() != null && optionalProfileCreateRequest.isPresent() &&
            optionalProfileCreateRequest.get().bytes() != null
            && optionalProfileCreateRequest.get().bytes().length > 0) {
            BinaryContent newProfile = updatedUser.getProfile();
            try {
                binaryContentStorage.put(newProfile.getId(),
                    optionalProfileCreateRequest.get().bytes());
                log.info("새 프로필 이미지 실제 파일 저장 완료. 프로필 ID: {}", newProfile.getId());
            } catch (Exception e) {
                log.error("사용자 ID '{}', 새 프로필 ID '{}'의 실제 파일 저장 중 오류 발생", userId,
                    newProfile.getId(), e);
                throw new FileProcessingCustomException("save-new-profile",
                    newProfile.getFileName(), "사용자 " + userId + "의 새 프로필 이미지 파일 저장 중 오류가 발생했습니다.");
            }
        }

        log.info("사용자 업데이트 로직 종료. ID: {}", userId);
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        log.info("사용자 삭제 시작. ID: '{}'", userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if ("admin".equalsIgnoreCase(user.getUsername())) {
            log.warn("Attempt to delete restricted user: {}", userId);
            throw new UserOperationRestrictedException(userId.toString(), "delete",
                "관리자 계정(" + user.getUsername() + ")은 삭제할 수 없습니다.");
        }

        log.info("사용자 ID '{}'가 작성한 메시지들의 작성자 정보를 null로 변경 시작.", userId);
        List<Message> messagesToUpdate = messageRepository.findByAuthorId(userId);
        if (!messagesToUpdate.isEmpty()) {
            for (Message message : messagesToUpdate) {
                message.setAuthor(null);
            }
            messageRepository.saveAll(messagesToUpdate);
            log.info("총 {}개의 메시지에서 작성자 정보를 null로 변경 완료.", messagesToUpdate.size());
        } else {
            log.info("사용자 ID '{}'가 작성한 메시지가 없습니다.", userId);
        }

        log.info("사용자 엔티티 삭제 시도");
        userRepository.delete(user);
        log.info("사용자 삭제 완료. ID: '{}'", userId);
    }

    private void validateUserDoesNotExist(String username, String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("유효성 검사 실패: 이메일 '{}'은(는) 이미 존재합니다.", email);
            throw new UserAlreadyExistException(ErrorCode.USER_ALREADY_EXISTS,
                Map.of("email", email));
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("유효성 검사 실패: 사용자명 '{}'은(는) 이미 존재합니다.", username);
            throw new UserAlreadyExistException(ErrorCode.USER_ALREADY_EXISTS,
                Map.of("username", username));
        }
    }
}