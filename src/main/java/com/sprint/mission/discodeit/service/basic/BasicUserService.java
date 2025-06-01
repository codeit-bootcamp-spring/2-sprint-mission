package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserOperationRestrictedException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(BasicUserService.class);

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;
    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;

    private static Map<String, Object> getStringObjectMap(String v, String originalFilename,
        Exception e, String userId) {
        Map<String, Object> newDetails = new HashMap<>();
        newDetails.put("operation", v);
        newDetails.put("filePath", originalFilename != null ? originalFilename : "unknown");
        newDetails.put("originalExceptionType", e.getClass().getName());
        if (e.getMessage() != null) {
            newDetails.put("originalExceptionMessage", e.getMessage());
        }
        newDetails.put("customMessageContext", userId);
        return newDetails;
    }

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest, MultipartFile profileImageFile) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();
        validateUserDoesNotExist(username, email);

        BinaryContent profileEntity = processProfileImage(profileImageFile, username);

        User user = new User(username, email, userCreateRequest.password(), profileEntity);
        User createdUser = userRepository.save(user);
        log.info("사용자 정보 저장 완료");

        UserStatus userStatus = new UserStatus(createdUser);
        userStatusRepository.save(userStatus);
        createdUser.setUserStatus(userStatus);
        log.info("사용자 상태 정보 생성 완료");

        UserDto userDto = userMapper.toDto(createdUser);
        log.info("사용자 생성 완료");
        return userDto;
    }

    private BinaryContent processProfileImage(MultipartFile profileImageFile, String username) {
        if (profileImageFile == null || profileImageFile.isEmpty()) {
            return null;
        }

        String originalFilename = profileImageFile.getOriginalFilename();
        log.debug("Attempting to process profile image for user {}: {}", username,
            originalFilename);
        try {
            BinaryContentDto profileDto = binaryContentService.create(profileImageFile);
            log.debug("Profile DTO created: {}", profileDto);
            return binaryContentRepository.findById(profileDto.id()).orElseThrow(() -> {
                log.error("저장된 프로필 이미지 메타데이터를 찾을 수 없습니다. ID: {}", profileDto.id());
                return new FileProcessingCustomException(
                    Map.of("operation", "create-user-profile-lookup", "filePath",
                        originalFilename != null ? originalFilename : "unknown",
                        "customMessageContext",
                        "저장된 프로필 이미지 메타데이터를 찾을 수 없습니다. ID: " + profileDto.id()));
            });
        } catch (Exception e) {
            log.error("Profile image processing error for user {}, file {}: {}", username,
                originalFilename, e.getMessage(), e);
            if (e instanceof FileProcessingCustomException fe) {
                throw new FileProcessingCustomException(new HashMap<>(fe.getDetails()));
            } else {
                Map<String, Object> newDetails = getStringObjectMap(
                    "create-user-profile-general-error", originalFilename, e,
                    "사용자 " + username + "의 프로필 이미지 처리 중 일반 오류 발생");
                throw new FileProcessingCustomException(newDetails);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto find(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId.toString()));
        log.info("사용자 조회 완료");
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        log.info("사용자 조회 완료");
        return userMapper.toDto(users);
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        MultipartFile profileImageFile) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if ("admin".equalsIgnoreCase(user.getUsername())) {
            log.warn("관리자 계정 수정 불가");
            throw new UserOperationRestrictedException(userId.toString(), "update",
                "관리자 계정(" + user.getUsername() + ")은 수정할 수 없습니다.");
        }

        String newUsername = userUpdateRequest.newUsername();
        String newEmail = userUpdateRequest.newEmail();

        if (newUsername != null && !newUsername.isEmpty() && !user.getUsername()
            .equals(newUsername)) {
            if (userRepository.existsByUsername(newUsername)) {
                log.warn("사용자명 중복");
                throw new UserAlreadyExistException(Map.of("username", newUsername));
            }
            user.setUsername(newUsername);
            log.info("사용자명 변경 완료");
        }

        if (newEmail != null && !newEmail.isEmpty() && !user.getEmail().equals(newEmail)) {
            if (userRepository.existsByEmail(newEmail)) {
                log.warn("이메일 중복");
                throw new UserAlreadyExistException(Map.of("email", newEmail));
            }
            user.setEmail(newEmail);
            log.info("이메일 변경 완료");
        }

        if (userUpdateRequest.newPassword() != null && !userUpdateRequest.newPassword().isEmpty()) {
            user.setPassword(userUpdateRequest.newPassword());
            log.info("비밀번호 변경 완료");
        }

        BinaryContent oldProfile = user.getProfile();

        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            String originalFilename = profileImageFile.getOriginalFilename();
            try {
                BinaryContentDto newProfileDto = binaryContentService.create(profileImageFile);
                BinaryContent newProfileEntityFromDb = binaryContentRepository.findById(
                    newProfileDto.id()).orElseThrow(() -> new FileProcessingCustomException(
                    Map.of("operation", "update-user-profile-lookup", "filePath",
                        originalFilename != null ? originalFilename : "unknown",
                        "customMessageContext",
                        "저장된 새 프로필 이미지 메타데이터를 찾을 수 없습니다. ID: " + newProfileDto.id())));
                log.info("프로필 이미지 저장 완료");

                if (oldProfile != null) {
                    try {
                        binaryContentService.delete(oldProfile.getId());
                        log.info("기존 프로필 이미지 삭제 완료");
                    } catch (Exception ex) {
                        log.error("기존 프로필 이미지 삭제 중 오류 발생. ID: {}", oldProfile.getId(), ex);
                    }
                }
                user.setProfile(newProfileEntityFromDb);
            } catch (Exception e) {
                log.error("Update - Profile image processing error for user ID {}, file {}: {}",
                    userId, originalFilename, e.getMessage(), e);
                if (e instanceof FileProcessingCustomException fe) {
                    throw new FileProcessingCustomException(new HashMap<>(fe.getDetails()));
                } else {
                    Map<String, Object> newDetails = getStringObjectMap(
                        "update-user-profile-general-error", originalFilename, e,
                        "사용자 ID " + userId + "의 새 프로필 이미지 처리 중 일반 오류 발생");
                    throw new FileProcessingCustomException(newDetails);
                }
            }
        }

        User updatedUser = userRepository.save(user);

        log.info("사용자 업데이트 완료");
        return userMapper.toDto(updatedUser);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId.toString()));

        if ("admin".equalsIgnoreCase(user.getUsername())) {
            log.warn("관리자 계정 삭제 불가");
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

        BinaryContent userProfile = user.getProfile();
        if (userProfile != null) {
            log.info("사용자 ID '{}'의 프로필 이미지 삭제 시도. 프로필 ID: {}", userId, userProfile.getId());
            try {
                binaryContentService.delete(userProfile.getId());
                log.info("사용자 프로필 이미지 삭제 완료. 프로필 ID: {}", userProfile.getId());
            } catch (Exception e) {
                log.error("사용자 ID '{}'의 프로필 이미지(ID: '{}') 삭제 중 오류 발생. 사용자 삭제는 계속 진행.", userId,
                    userProfile.getId(), e);
            }
        }

        log.info("사용자 엔티티 삭제 시도");
        userRepository.delete(user);
        log.info("사용자 삭제 완료. ID: '{}'", userId);
    }

    private void validateUserDoesNotExist(String username, String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("유효성 검사 실패: 이메일 '{}'은(는) 이미 존재합니다.", email);
            throw new UserAlreadyExistException(Map.of("email", email));
        }
        if (userRepository.existsByUsername(username)) {
            log.warn("유효성 검사 실패: 사용자명 '{}'은(는) 이미 존재합니다.", username);
            throw new UserAlreadyExistException(Map.of("username", username));
        }
    }
}