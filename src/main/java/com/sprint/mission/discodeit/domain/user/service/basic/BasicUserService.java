package com.sprint.mission.discodeit.domain.user.service.basic;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentCore;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.domain.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.domain.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.domain.user.exception.UserAlreadyExistsException;
import com.sprint.mission.discodeit.domain.user.exception.UserNotFoundException;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.domain.user.service.UserService;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.mapper.UserResultMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        return userResultMapper.convertToUserResult(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResult getByName(String name) {
        log.debug("사용자 이름으로 조회 요청: name={}", name);
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException(Map.of("userName", name)));

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
                .orElseThrow(() -> new UserNotFoundException(Map.of("userEmail", email)));

        return userResultMapper.convertToUserResult(user);
    }

    @Transactional
    @Override
    public UserResult update(UUID userId, UserUpdateRequest userUpdateRequest, BinaryContentRequest binaryContentRequest) {
        log.info("사용자 수정 요청: userId={}, newUsername={}, newEmail={}", userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

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
            throw new UserNotFoundException(Map.of("userId", userId));
        }

        userRepository.deleteById(userId);
        log.info("사용자 삭제 완료: userId={}", userId);
    }

    private void validateDuplicateUserName(String name) {
        if (userRepository.existsUserByName(name)) {
            log.warn("중복된 사용자 이름: {}", name);
            throw new UserAlreadyExistsException(Map.of("userName", name));
        }
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsUserByEmail(email)) {
            log.warn("중복된 이메일: {}", email);
            throw new UserAlreadyExistsException(Map.of("email", email));
        }
    }

}
