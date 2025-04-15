package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentJPARepository;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.repository.UserStatusJPARepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.userdto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserJPARepository userJPARepository;
    private final UserStatusJPARepository userStatusJPARepository;
    private final BinaryContentJPARepository binaryContentJPARepository;

    @Override
    @Transactional
    public User create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {

        if (userJPARepository.existsByUsernameOrEmail(userCreateDto.username(), userCreateDto.email())) {
            throw new InvalidInputException("Username or email already exists");
        }

        BinaryContent nullableProfile = optionalBinaryContentCreateDto
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentJPARepository.save(binaryContent);
                })
                .orElse(null);

        // profile ID 추가 후 user 생성
        User user = new User(userCreateDto.username(), userCreateDto.email(), userCreateDto.password(), nullableProfile);
        User createdUser = userJPARepository.save(user);

        // user status 생성
        Instant currentTime = Instant.now();
        UserStatus userStatus = new UserStatus(createdUser, currentTime);
        userStatusJPARepository.save(userStatus);

        return createdUser;
    }


    @Override
    public UserFindResponseDto find(UserFindRequestDto userFindRequestDto) {
        // userRepository 에서 User Id로 조회
        User matchingUser = userJPARepository.findById(userFindRequestDto.userId())
                .orElseThrow(() -> new NotFoundException("User does not exist."));

        // userStatusJPARepository 에서 User Id로 조회
        UserStatus matchingUserStatus = userStatusJPARepository.findById(userFindRequestDto.userId())
                .orElseThrow(() -> new NotFoundException("User Status does not exist."));

        return UserFindResponseDto.UserFindResponse(matchingUser, matchingUserStatus);
    }


    @Override
    public List<UserFindAllResponseDto> findAllUser() {
        List<User> userAllList = userJPARepository.findAll().stream().toList();
        if (userAllList.isEmpty()) {
            throw new NotFoundException("User list is empty.");
        }

        return UserFindAllResponseDto.UserFindAllResponse(userAllList);
    }


    @Override
    @Transactional
    public User update(UUID userId, UserUpdateDto userUpdateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        User matchingUser = userJPARepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist."));

        BinaryContent nullableProfile = optionalBinaryContentCreateDto
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentJPARepository.save(binaryContent);
                })
                .orElse(null);

        if (userUpdateDto.newPassword() == null) {
            matchingUser.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), matchingUser.getPassword(), nullableProfile);
        } else {
            matchingUser.update(userUpdateDto.newUsername(), userUpdateDto.newEmail(), userUpdateDto.newPassword(), nullableProfile);
        }
        userJPARepository.save(matchingUser);

        return matchingUser;
    }


    @Override
    @Transactional
    public void delete(UUID userId) {
        User matchingUser = userJPARepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist."));
        userJPARepository.delete(matchingUser);
    }
}
