package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.NotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserJPARepository;
import com.sprint.mission.discodeit.repository.UserStatusJPARepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusDeleteDto;
import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusFindDto;
import com.sprint.mission.discodeit.service.dto.request.userstatusdto.UserStatusUpdateDto;
import com.sprint.mission.discodeit.service.dto.response.UserStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusJPARepository userStatusJPARepository;
    private final UserJPARepository userJPARepository;
    private final UserStatusMapper userStatusMapper;

    @Override
    @Transactional
    public UserStatusResponseDto create(UserStatusCreateDto userStatusCreateDto) {
        User matchingUser = userJPARepository.findById(userStatusCreateDto.userId())
                .orElseThrow(() -> new NotFoundException("User does not exist."));

        Instant currentTime = Instant.now();
        UserStatus userStatus = new UserStatus(matchingUser, currentTime);
        userStatusJPARepository.save(userStatus);

        return userStatusMapper.toDto(userStatus);
    }


    @Override
    @Transactional(readOnly = true)
    public UserStatusResponseDto find(UserStatusFindDto userStatusFindDto) {
        UserStatus userStatus = userStatusJPARepository.findByUser_Id(userStatusFindDto.userId())
                .orElseThrow(() -> new NotFoundException("User does not exist."));
        return userStatusMapper.toDto(userStatus);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserStatusResponseDto> findAll() {
        List<UserStatusResponseDto> userStatusList = new ArrayList<>();
        userStatusJPARepository.findAll().stream()
                .map(userStatusMapper::toDto)
                .forEach(userStatusList::add);
        return userStatusList;
    }


    @Override
    @Transactional
    public UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateDto userStatusUpdateRequest) {
        UserStatus matchingUserStatus = userStatusJPARepository.findByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException("UserStatus does not exist."));

        Instant currentTime = userStatusUpdateRequest.newLastActiveAt();
        matchingUserStatus.updateLastConnectionTime(currentTime);
        UserStatus updateUserStatus = userStatusJPARepository.save(matchingUserStatus);
        return userStatusMapper.toDto(updateUserStatus);
    }


    @Override
    @Transactional
    public void delete(UserStatusDeleteDto userStatusDeleteDto) {
        UserStatus matchingUserStatus = userStatusJPARepository.findByUser_Id(userStatusDeleteDto.userId())
                .orElseThrow(() -> new NotFoundException("User does not exist."));
        userStatusJPARepository.delete(matchingUserStatus);
    }
}
