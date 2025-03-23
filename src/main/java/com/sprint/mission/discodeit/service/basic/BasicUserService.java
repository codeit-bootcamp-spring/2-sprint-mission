package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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

    @Override
    public User create(UserCreateDto userCreateDto) {
        List<User> userList = userRepository.load();
        Optional<User> validateName = userList.stream()
                .filter(u -> u.getName().equals(userCreateDto.name()))
                .findAny();
        Optional<User> validateEmail = userList.stream()
                .filter(u -> u.getEmail().equals(userCreateDto.email()))
                .findAny();
        if (validateName.isPresent()) {
            throw new IllegalArgumentException("User already exists.");
        }
        if (validateEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }

        // binary content 생성
        BinaryContentCreateDto binaryContentCreateDto = new BinaryContentCreateDto(userCreateDto.path());
        BinaryContent binaryContent = binaryContentService.create(binaryContentCreateDto);

        // profile ID 추가 후 user 생성
        User user = new User(userCreateDto.name(), userCreateDto.email(), userCreateDto.password());
        UUID profileId =  (binaryContent != null) ? binaryContent.getId() : null;
        user.update(userCreateDto.name(), userCreateDto.email(), userCreateDto.password(), profileId);
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
                .orElseThrow(() -> new NoSuchElementException("User does not exist."));

        // userStatusRepository 에서 User Id로 조회
        UserStatus matchingUserStatus = userStatusRepository.load().stream()
                .filter(u -> u.getUserId().equals(userFindRequestDto.userId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User Status does not exist."));

        return UserFindResponseDto.UserFindResponse(matchingUser, matchingUserStatus);
    }


    @Override
    public List<UserFindAllResponseDto> findAllUser() {
        List<User> userList = userRepository.load().stream().toList();
        if (userList.isEmpty()) {
            throw new NoSuchElementException("User list is empty.");
        }
        List<UserStatus> userStatusList = userStatusRepository.load().stream().toList();
        if (userStatusList.isEmpty()) {
            throw new NoSuchElementException("User Status list is empty.");
        }

        return UserFindAllResponseDto.UserFindAllResponse(userList, userStatusList);
    }


    @Override
    public User update(UserUpdateDto userUpdateDto) {
        User matchingUser = userRepository.load().stream()
                .filter(u -> u.getId().equals(userUpdateDto.userId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User does not exist."));

        BinaryContentCreateDto binaryContentCreateDto = new BinaryContentCreateDto(userUpdateDto.path());
        BinaryContent binaryContent = binaryContentService.create(binaryContentCreateDto);

        BinaryContentDeleteDto binaryContentDeleteDto = new BinaryContentDeleteDto(matchingUser.getProfileId());
        binaryContentService.delete(binaryContentDeleteDto);

        UUID profileId =  (binaryContent != null) ? binaryContent.getId() : matchingUser.getProfileId();
        matchingUser.update(userUpdateDto.changeName(), userUpdateDto.changeEmail(), userUpdateDto.changePassword(), profileId);
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
                .orElseThrow(() -> new NoSuchElementException("User does not exist."));

        UserStatus matchingUserStatus = userStatusRepository.load().stream()
                .filter(us -> us.getUserId().equals(matchingUser.getId()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User Status does not exist."));

        BinaryContentDeleteDto binaryContentDeleteDto = new BinaryContentDeleteDto(matchingUser.getProfileId());
        binaryContentService.delete(binaryContentDeleteDto);

        userStatusRepository.remove(matchingUserStatus);

        userRepository.remove(matchingUser);
    }
}
