package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.service.user.UserResult;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public UserResult register(UserCreateRequest userRequest, BinaryContentRequest binaryContentRequest) {
        validateDuplicateEmail(userRequest.email());
        validateDuplicateUserName(userRequest.username());

        BinaryContent savedBinaryContent = null;
        if (binaryContentRequest != null) {
            BinaryContent binaryContent = new BinaryContent(
                    binaryContentRequest.fileName(),
                    binaryContentRequest.contentType());

            binaryContentStorage.put(binaryContent.getId(), binaryContentRequest.bytes());
            savedBinaryContent = binaryContentRepository.save(binaryContent);
        }

        User savedUser = userRepository.save(new User(
                userRequest.username(),
                userRequest.email(),
                userRequest.password(),
                savedBinaryContent
        ));
        Instant now = Instant.now();
        UserStatus userStatus = userStatusRepository.save(new UserStatus(savedUser, now));

        return UserResult.fromEntity(savedUser, userStatus.isOnline(now));
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

        UserStatus userStatus = userStatusRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        return UserResult.fromEntity(user, userStatus.isOnline(Instant.now()));
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        return UserResult.fromEntity(user, userStatus.isOnline(Instant.now()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResult> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                            .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

                    return UserResult.fromEntity(user, userStatus.isOnline(Instant.now()));
                })
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));

        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        return UserResult.fromEntity(user, userStatus.isOnline(Instant.now()));
    }

    @Transactional
    @Override
    public UserResult update(UUID userId, UserUpdateRequest userUpdateRequest, BinaryContentRequest binaryContentRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

        BinaryContent savedBinaryContent = null;
        if (binaryContentRequest != null) {
            BinaryContent binaryContent = new BinaryContent(
                    binaryContentRequest.fileName(),
                    binaryContentRequest.contentType());

            savedBinaryContent = binaryContentRepository.save(binaryContent);
            binaryContentStorage.put(binaryContent.getId(), binaryContentRequest.bytes());

            binaryContentRepository.deleteById(user.getBinaryContent().getId());
        }

        user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), userUpdateRequest.newPassword(), savedBinaryContent);

        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 유저Id를 가진 UserStatus가 없습니다."));

        return UserResult.fromEntity(user, userStatus.isOnline(Instant.now()));
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        userRepository.deleteById(userId);

        UserStatus userStatus = userStatusRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent()));

        userStatusRepository.deleteById(userStatus.getId());
    }

    private void validateDuplicateUserName(String name) {
        boolean isDuplicate = userRepository.findByName(name)
                .isPresent();

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 이름 입니다");
        }
    }

    private void validateDuplicateEmail(String requestEmail) {
        boolean isDuplicate = userRepository.findAll()
                .stream()
                .anyMatch(existingUser -> existingUser.isSameEmail(requestEmail));

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
    }
}
