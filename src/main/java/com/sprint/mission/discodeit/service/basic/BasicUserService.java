package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.ErrorCode;
import com.sprint.mission.discodeit.exceptions.user.DuplicateUserOrEmailException;
import com.sprint.mission.discodeit.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ResponseMapStruct;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(BasicUserService.class);

    private final UserJPARepository userJpaRepository;
    private final BinaryContentJPARepository binaryContentJpaRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final ResponseMapStruct responseMapStruct;


    @Override
    @Transactional
    public UserResponseDto create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        logger.debug("[User][create] Calling userJpaRepository.existsByUsernameOrEmail(): username={}, email={}", userCreateDto.username(), userCreateDto.email());

        if (userJpaRepository.existsByUsernameOrEmail(userCreateDto.username(), userCreateDto.email())) {
            logger.warn("[User][create] Username or email already exists: username={}, email={}", userCreateDto.username(), userCreateDto.email());
            throw new DuplicateUserOrEmailException(Instant.now(), ErrorCode.DUPLICATE_USER_OR_EMAIL, Map.of("userName", userCreateDto.username(), "email", userCreateDto.email()));
        }

        BinaryContent nullableProfile = mapProfile(optionalBinaryContentCreateDto);

        logger.debug("[User][create] Entity constructed: username={}, email={}", userCreateDto.username(), userCreateDto.email());
        User user = new User(userCreateDto.username(), userCreateDto.email(), userCreateDto.password(), nullableProfile);
        new UserStatus(user, Instant.now());
        logger.debug("[User][create] Calling userJpaRepository.save()");
        User createdUser = userJpaRepository.save(user);
        logger.info("[User][create] Created successfully: userId={}", createdUser.getId());
        return responseMapStruct.toUserDto(createdUser);
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDto find(UUID userId) {
        User matchingUser = userJpaRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));
        return responseMapStruct.toUserDto(matchingUser);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUser() {
        List<UserResponseDto> userAllList = new ArrayList<>();
        userJpaRepository.findAllUsers().stream()
                .map(responseMapStruct::toUserDto)
                .forEach(userAllList::add);
        if (userAllList.isEmpty()) {
            logger.warn("[User][findAllUser] User list is empty.");
            throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, Map.of("userId", List.of()));
        }
        return userAllList;
    }


    @Override
    @Transactional
    public UserResponseDto update(UUID userId, UserUpdateDto userUpdateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        logger.debug("[User][update] Calling userJpaRepository.findById(): userId={}", userId);
        User matchingUser = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));

        BinaryContent nullableProfile = mapProfile(optionalBinaryContentCreateDto);

        if (userUpdateDto.newPassword() == null || userUpdateDto.newPassword().isEmpty()) {
            logger.debug("[User][update] New password is empty.");
            matchingUser.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), matchingUser.getPassword(), nullableProfile);
        } else {
            logger.debug("[User][update] New password is not empty.");
            matchingUser.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), userUpdateDto.newPassword(), nullableProfile);
        }
        logger.debug("[User][update] Calling userJpaRepository.save()");
        userJpaRepository.save(matchingUser);
        logger.info("[User][update] Updated successfully: userId={}", userId);
        return responseMapStruct.toUserDto(matchingUser);
    }


    @Override
    @Transactional
    public void delete(UUID userId) {
        logger.debug("[User][delete] Calling userJpaRepository.findById(): userId={}", userId);
        User matchingUser = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));

        logger.debug("[User][delete] Calling userJpaRepository.delete(): userId={}", userId);
        userJpaRepository.delete(matchingUser);
        logger.info("[User][delete] Deleted successfully: userId={}", userId);
    }


    // user profile 생성 및 수정
    private BinaryContent mapProfile(Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        return optionalBinaryContentCreateDto.map(profileRequest -> {
                    logger.debug("[User] Starting profile upload process: filename:{}, type:{}", profileRequest.fileName(), profileRequest.contentType());
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
                    BinaryContent updateBinaryContent = binaryContentJpaRepository.save(binaryContent);
                    binaryContentStorage.put(updateBinaryContent.getId(), bytes);
                    logger.debug("[User] Profile upload completed process: filename:{}, type:{}", profileRequest.fileName(), profileRequest.contentType());
                    return updateBinaryContent;
                })
                .orElse(null);
    }
}
