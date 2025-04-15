package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.repository.UserStatusJPARepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusFindDto;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusJPARepository userStatusJPARepository;
    private final UserJPARepository userJPARepository;

    @Override
    @Transactional
    public UserStatus create(UserStatusCreateDto userStatusCreateDto) {
        User matchingUser = userJPARepository.findById(userStatusCreateDto.userId())
                .orElseThrow(() -> new NotFoundException("User does not exist."));

        Instant currentTime = Instant.now();
        UserStatus userStatus = new UserStatus(matchingUser, currentTime);
        userStatusJPARepository.save(userStatus);
        return userStatus;
    }


    @Override
    public UserStatus find(UserStatusFindDto userStatusFindDto) {
        return userStatusJPARepository.findById(userStatusFindDto.userId())
                .orElseThrow(() -> new NotFoundException("User does not exist."));
    }


    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> userStatusList = userStatusJPARepository.findAll();
        if (userStatusList.isEmpty()) {
            throw new NotFoundException("Profile not found.");
        }
        return userStatusList;
    }


    @Override
    @Transactional
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateDto userStatusUpdateRequest) {
        UserStatus matchingUserStatus = userStatusJPARepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User does not exist."));

        Instant currentTime = userStatusUpdateRequest.newLastActiveAt();
        matchingUserStatus.updateLastConnectionTime(currentTime);
        return userStatusJPARepository.save(matchingUserStatus);
    }


    @Override
    @Transactional
    public void delete(UserStatusDeleteDto userStatusDeleteDto) {
        UserStatus matchingUserStatus = userStatusJPARepository.findById(userStatusDeleteDto.userId())
                .orElseThrow(() -> new NotFoundException("User does not exist."));
        userStatusJPARepository.delete(matchingUserStatus);
    }
}
