package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.exceptions.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.ResponseMapStruct;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.repository.UserStatusJPARepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.UserStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusJPARepository userStatusJPARepository;
    private final UserJPARepository userJPARepository;
    private final UserStatusMapper userStatusMapper;
    private final ResponseMapStruct responseMapStruct;

    @Override
    @Transactional
    public UserStatusResponseDto create(UserStatusCreateDto userStatusCreateDto) {
        User matchingUser = userJPARepository.findById(userStatusCreateDto.userId())
                .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userStatusCreateDto.userId())));

        Instant currentTime = Instant.now();
        UserStatus userStatus = new UserStatus(matchingUser, currentTime);
        userStatusJPARepository.save(userStatus);

        return responseMapStruct.toUserStatusDto(userStatus);
    }


    @Override
    @Transactional(readOnly = true)
    public UserStatusResponseDto find(UUID userId) {
        UserStatus userStatus = userStatusJPARepository.findByUser_Id(userId)
                .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", userId)));
        return responseMapStruct.toUserStatusDto(userStatus);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserStatusResponseDto> findAll() {
        List<UserStatusResponseDto> userStatusList = new ArrayList<>();
        userStatusJPARepository.findAll().stream()
                .map(responseMapStruct::toUserStatusDto)
                .forEach(userStatusList::add);
        return userStatusList;
    }


    @Override
    @Transactional
    public UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateDto userStatusUpdateRequest) {
        UserStatus matchingUserStatus = userStatusJPARepository.findByUser_Id(userId)
                .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", userId)));

        Instant currentTime = userStatusUpdateRequest.newLastActiveAt();
        matchingUserStatus.updateLastConnectionTime(currentTime);
        UserStatus updateUserStatus = userStatusJPARepository.save(matchingUserStatus);
        return responseMapStruct.toUserStatusDto(updateUserStatus);
    }


    @Override
    @Transactional
    public void delete(UUID userId) {
        UserStatus matchingUserStatus = userStatusJPARepository.findByUser_Id(userId)
                .orElseThrow(() -> new UserStatusNotFoundException(Map.of("userId", userId)));
        userStatusJPARepository.delete(matchingUserStatus);
    }
}
