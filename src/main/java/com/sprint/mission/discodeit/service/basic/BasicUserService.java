package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserJPARepository userJpaRepository;
    private final BinaryContentJPARepository binaryContentJpaRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final ResponseMapStruct responseMapStruct;

    @Override
    @Transactional
    public UserResponseDto create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        if (userJpaRepository.existsByUsernameOrEmail(userCreateDto.username(), userCreateDto.email())) {
            throw new InvalidInputException("Username or email already exists");
        }

        BinaryContent nullableProfile = optionalBinaryContentCreateDto
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
                    BinaryContent createBinaryContent = binaryContentJpaRepository.save(binaryContent);
                    binaryContentStorage.put(createBinaryContent.getId(), bytes);
                    return binaryContent;
                })
                .orElse(null);

        User user = new User(userCreateDto.username(), userCreateDto.email(), userCreateDto.password(), nullableProfile);
        new UserStatus(user, Instant.now());
        User createdUser = userJpaRepository.save(user);
        return responseMapStruct.toUserDto(createdUser);
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDto find(UUID userId) {
        User matchingUser = userJpaRepository.findByIdWithProfile(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist."));
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
            throw new NotFoundException("User list is empty.");
        }
        return userAllList;
    }


    @Override
    @Transactional
    public UserResponseDto update(UUID userId, UserUpdateDto userUpdateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        User matchingUser = userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist."));

        BinaryContent nullableProfile = optionalBinaryContentCreateDto
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
                    BinaryContent updateBinaryContent = binaryContentJpaRepository.save(binaryContent);
                    binaryContentStorage.put(updateBinaryContent.getId(), bytes);
                    return updateBinaryContent;

                })
                .orElse(null);

        if (userUpdateDto.newPassword() == null) {
            matchingUser.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), matchingUser.getPassword(), nullableProfile);
        } else {
            matchingUser.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), userUpdateDto.newPassword(), nullableProfile);
        }
        userJpaRepository.save(matchingUser);
        return responseMapStruct.toUserDto(matchingUser);
    }


    @Override
    @Transactional
    public void delete(UUID userId) {
        User matchingUser = userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist."));
        userJpaRepository.delete(matchingUser);
    }
}
