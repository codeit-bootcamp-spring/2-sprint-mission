package com.sprint.mission.discodeit.user.service.basic;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentCore;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.mapper.UserResultMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.sprint.mission.discodeit.common.constant.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.common.constant.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentCore binaryContentService;
    private final UserResultMapper userResultMapper;

    @Transactional
    @Override
    public UserResult register(UserCreateRequest userRequest, BinaryContentRequest binaryContentRequest) {
        log.info("사용자 생성 요청: username={}, email={}", userRequest.username(), userRequest.email());
        validateDuplicateEmail(userRequest.email());
        validateDuplicateUserName(userRequest.username());

        BinaryContent binaryContent = binaryContentService.createBinaryContent(binaryContentRequest);
        User savedUser = userRepository.save(new User(userRequest.username(), userRequest.email(), userRequest.password(), binaryContent));
        log.info("사용자 생성 완료: userId={}", savedUser.getId());

        return UserResult.fromEntity(savedUser, savedUser.getUserStatus().isOnline(Instant.now()));
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getById(UUID userId) {
        log.debug("사용자 조회 요청: userId={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("사용자 조회 실패: userId={} (존재하지 않음)", userId);
                    return new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent());
                });

        return userResultMapper.convertToUserResult(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getByName(String name) {
        log.debug("사용자 이름으로 조회 요청: name={}", name);
        User user = userRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("사용자 이름 조회 실패: name={} (존재하지 않음)", name);
                    return new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent());
                });

        return userResultMapper.convertToUserResult(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResult> getAllIn() {
        log.debug("전체 사용자 목록 조회 요청");
        return userRepository.findAll()
                .stream()
                .map(userResultMapper::convertToUserResult)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getByEmail(String email) {
        log.debug("사용자 이메일로 조회 요청: email={}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("사용자 이메일 조회 실패: email={} (존재하지 않음)", email);
                    return new EntityNotFoundException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent());
                });

        return userResultMapper.convertToUserResult(user);
    }

    @Transactional
    @Override
    public UserResult update(UUID userId, UserUpdateRequest userUpdateRequest, BinaryContentRequest binaryContentRequest) {
        log.info("사용자 수정 요청: userId={}, newUsername={}, newEmail={}", userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("사용자 수정 실패: userId={} (존재하지 않음)", userId);
                    return new EntityNotFoundException(ERROR_USER_NOT_FOUND.getMessageContent());
                });

        if (user.getBinaryContent() != null) {
            log.debug("기존 BinaryContent 삭제: id={}", user.getBinaryContent().getId());
            binaryContentService.delete(user.getBinaryContent().getId());
        }

        BinaryContent binaryContent = binaryContentService.createBinaryContent(binaryContentRequest);
        user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail(), userUpdateRequest.newPassword(), binaryContent);
        User updatedUser = userRepository.save(user);

        log.info("사용자 수정 완료: userId={}", updatedUser.getId());
        return userResultMapper.convertToUserResult(updatedUser);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        log.warn("사용자 삭제 요청: userId={}", userId);
        if (!userRepository.existsById(userId)) {
            log.error("사용자 삭제 실패: userId={} (존재하지 않음)", userId);
            throw new NoSuchElementException("User with id " + userId + " not found");
        }

        userRepository.deleteById(userId);
        log.info("사용자 삭제 완료: userId={}", userId);
    }

    private void validateDuplicateUserName(String name) {
        if (userRepository.existsUserByName(name)) {
            log.warn("중복된 사용자 이름: {}", name);
            throw new IllegalArgumentException("이미 존재하는 이름 입니다");
        }
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsUserByEmail(email)) {
            log.warn("중복된 이메일: {}", email);
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
    }

}
