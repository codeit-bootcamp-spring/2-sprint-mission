package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.InvalidInputException;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentDeleteDto;
import com.sprint.mission.discodeit.service.dto.userdto.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User create(UserCreateDto userCreateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        List<User> userList = userRepository.load();
        Optional<User> validateName = userList.stream()
                .filter(u -> u.getName().equals(userCreateDto.name()))
                .findAny();
        Optional<User> validateEmail = userList.stream()
                .filter(u -> u.getEmail().equals(userCreateDto.email()))
                .findAny();
        if (validateName.isPresent()) {
            throw new InvalidInputException("User already exists.");
        }
        if (validateEmail.isPresent()) {
            throw new InvalidInputException("Email already exists.");
        }

        UUID nullableProfileId = optionalBinaryContentCreateDto
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        // profile ID 추가 후 user 생성
        User user = new User(userCreateDto.name(), userCreateDto.email(), userCreateDto.password(), nullableProfileId);
        User createdUser = userRepository.save(user);
        System.out.println(createdUser);

        // user status 생성
        Instant currentTime = Instant.now();
        UserStatus userStatus = new UserStatus(createdUser.getId(), currentTime);
        userStatusRepository.save(userStatus);

        return createdUser;
    }


    @Override
    public UserFindResponseDto find(UserFindRequestDto userFindRequestDto) {
        // userRepository 에서 User Id로 조회
        User matchingUser = userRepository.load().stream()
                .filter(u -> u.getId().equals(userFindRequestDto.userId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User does not exist."));

        // userStatusRepository 에서 User Id로 조회
        UserStatus matchingUserStatus = userStatusRepository.load().stream()
                .filter(u -> u.getUserId().equals(userFindRequestDto.userId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User Status does not exist."));

        return UserFindResponseDto.UserFindResponse(matchingUser, matchingUserStatus);
    }


    @Override
    public List<UserFindAllResponseDto> findAllUser() {
        List<User> userList = userRepository.load().stream().toList();
        if (userList.isEmpty()) {
            throw new NotFoundException("User list is empty.");
        }
        List<UserStatus> userStatusList = userStatusRepository.load().stream().toList();
        if (userStatusList.isEmpty()) {
            throw new NotFoundException("User Status list is empty.");
        }

        return UserFindAllResponseDto.UserFindAllResponse(userList, userStatusList);
    }


    @Override
    public User update(UserUpdateDto userUpdateDto, Optional<BinaryContentCreateDto> optionalBinaryContentCreateDto) {
        User matchingUser = userRepository.load().stream()
                .filter(u -> u.getId().equals(userUpdateDto.userId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User does not exist."));


        UUID nullableProfileId = optionalBinaryContentCreateDto
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        BinaryContentDeleteDto binaryContentDeleteDto = new BinaryContentDeleteDto(matchingUser.getProfileId());
        binaryContentService.delete(binaryContentDeleteDto);

        matchingUser.update(userUpdateDto.changeName(), userUpdateDto.changeEmail(), userUpdateDto.changePassword(), nullableProfileId);
        userRepository.save(matchingUser);

        UserStatusUpdateDto userStatusUpdateDto = new UserStatusUpdateDto(matchingUser.getId());
        userStatusService.updateByUserId(userStatusUpdateDto);
        
        return matchingUser;
    }


    @Override
    public void delete(UserDeleteDto userDeleteDto) {
        User matchingUser = userRepository.load().stream()
                .filter(u -> u.getId().equals(userDeleteDto.userId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User does not exist."));

        UserStatus matchingUserStatus = userStatusRepository.load().stream()
                .filter(us -> us.getUserId().equals(matchingUser.getId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User Status does not exist."));

        BinaryContentDeleteDto binaryContentDeleteDto = new BinaryContentDeleteDto(matchingUser.getProfileId());
        binaryContentService.delete(binaryContentDeleteDto);

        userStatusRepository.remove(matchingUserStatus);

        userRepository.remove(matchingUser);
    }
}
