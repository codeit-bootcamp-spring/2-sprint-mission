package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.user.DuplicateUserOrEmailException;
import com.sprint.mission.discodeit.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ResponseMapStruct;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.repository.UserStatusJPARepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userdto.UserUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

    private final UserJPARepository userJpaRepository;
    private final BinaryContentJPARepository binaryContentJpaRepository;
    private final UserStatusJPARepository userStatusJpaRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final ResponseMapStruct responseMapStruct;


    @Override
    @Transactional
    public UserResponseDto create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        log.debug("[User][create] Calling userJpaRepository.existsByUsernameOrEmail(): username={}, email={}", userCreateDto.username(), userCreateDto.email());

        if (userJpaRepository.existsByUsernameOrEmail(userCreateDto.username(), userCreateDto.email())) {
            log.warn("[User][create] Username or email already exists: username={}, email={}", userCreateDto.username(), userCreateDto.email());
            throw new DuplicateUserOrEmailException(Map.of("userName", userCreateDto.username(), "email", userCreateDto.email()));
        }

        BinaryContent nullableProfile = saveProfile(optionalBinaryContentCreateDto);

        log.debug("[User][create] Entity constructed: username={}, email={}", userCreateDto.username(), userCreateDto.email());
        User user = new User(userCreateDto.username(), userCreateDto.email(), userCreateDto.password(), nullableProfile);
        UserStatus userStatus = new UserStatus(user, Instant.now());
        log.debug("[User][create] Calling userJpaRepository.save()");
        User createdUser = userJpaRepository.save(user);
        log.info("[User][create] Created successfully: userId={}", createdUser.getId());
        return responseMapStruct.toUserDto(createdUser);
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDto find(UUID userId) {
        User matchingUser = userJpaRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
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
            log.warn("[User][findAllUser] User list is empty.");
            throw new UserNotFoundException(Map.of("userId", List.of()));
        }
        return userAllList;
    }


    @Override
    @Transactional
    public UserResponseDto update(UUID userId, UserUpdateDto userUpdateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        log.debug("[User][update] Calling userJpaRepository.findById(): userId={}", userId);
        User matchingUser = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        BinaryContent nullableProfile = saveProfile(optionalBinaryContentCreateDto);

        if (userUpdateDto.newPassword() == null || userUpdateDto.newPassword().isEmpty()) {
            log.debug("[User][update] New password is empty.");
            matchingUser.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), matchingUser.getPassword(), nullableProfile);
        } else {
            log.debug("[User][update] New password is not empty.");
            matchingUser.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), userUpdateDto.newPassword(), nullableProfile);
        }
        log.debug("[User][update] Calling userJpaRepository.save()");
        userJpaRepository.save(matchingUser);
        log.info("[User][update] Updated successfully: userId={}", userId);
        return responseMapStruct.toUserDto(matchingUser);
    }


    @Override
    @Transactional
    public void delete(UUID userId) {
        log.debug("[User][delete] Calling userJpaRepository.findById(): userId={}", userId);
        User matchingUser = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        log.debug("[User][delete] Calling userJpaRepository.delete(): userId={}", userId);
        userJpaRepository.delete(matchingUser);
        log.info("[User][delete] Deleted successfully: userId={}", userId);
    }


    // user profile 생성 및 수정
    private BinaryContent saveProfile(Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        return optionalBinaryContentCreateDto.map(profileRequest -> {
                    log.debug("[User] Starting profile upload process: filename:{}, type:{}", profileRequest.fileName(), profileRequest.contentType());
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
                    BinaryContent updateBinaryContent = binaryContentJpaRepository.save(binaryContent);
                    binaryContentStorage.put(updateBinaryContent.getId(), bytes);
                    log.debug("[User] Profile upload completed process: filename:{}, type:{}", profileRequest.fileName(), profileRequest.contentType());
                    return updateBinaryContent;
                })
                .orElse(null);
    }
}
