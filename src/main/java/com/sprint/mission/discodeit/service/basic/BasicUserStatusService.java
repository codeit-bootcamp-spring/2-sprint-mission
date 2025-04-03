package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusFindDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateDto userStatusCreateDto) {
        List<User> userList = userRepository.load();
        Optional<User> validateName = userList.stream()
                .filter(u -> u.getId().equals(userStatusCreateDto.userId()))
                .findAny();
        if (validateName.isEmpty()) {
            throw new NotFoundException("User does not exist.");
        }
        Instant currentTime = Instant.now();
        UserStatus userStatus = new UserStatus(userStatusCreateDto.userId(), currentTime);
        userStatusRepository.save(userStatus);
        return userStatus;
    }


    @Override
    public UserStatus getUser(UserStatusFindDto userStatusFindDto) {
        Optional<UserStatus> userStatus = userStatusRepository.load().stream()
                .filter(u -> u.getUserId().equals(userStatusFindDto.userId()))
                .findAny();
        return userStatus.orElseThrow(() -> new NotFoundException("User does not exist."));
    }


    @Override
    public List<UserStatus> getAllUser() {
        List<UserStatus> userStatusList = userStatusRepository.load();
        if (userStatusList.isEmpty()) {
            throw new NotFoundException("Profile not found.");
        }
        return userStatusList;
    }


    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus matchingUserStatus = userStatusRepository.load().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User does not exist."));
        Instant currentTime = Instant.now();
        matchingUserStatus.updateLastConnectionTime(currentTime);
        userStatusRepository.save(matchingUserStatus);
        return matchingUserStatus;
    }


    @Override
    public void delete(UserStatusDeleteDto userStatusDeleteDto) {
        UserStatus matchingUserStatus = userStatusRepository.load().stream()
                .filter(u -> u.getUserId().equals(userStatusDeleteDto.userId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User does not exist."));
        userStatusRepository.remove(matchingUserStatus);
    }
}
