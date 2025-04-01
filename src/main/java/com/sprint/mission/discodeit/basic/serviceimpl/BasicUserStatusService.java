package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.StatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DataConflictException;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapping.UserStatusMapping;
import com.sprint.mission.discodeit.service.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.StatusOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;


    @Override
    public boolean createUserStatus(UUID userId) {
        userStatusRepository.findByUserId(userId)
                .ifPresent(existing -> {
                    throw new DataConflictException("UserStatus", "userId", userId);
                });

        UserStatus userStatus = new UserStatus(userId);
        userStatusRepository.register(userStatus);
        return true;
    }

    @Override
    public StatusDto.StatusResponse updateUserStatus(UUID userId, StatusDto.StatusRequest statusDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("UserStatus", "userId", userId));
        try {
            StatusOperation requestedStatus = StatusOperation.valueOf(statusDto.getStatus().toUpperCase());
            userStatus.setStatus(requestedStatus);
            return new StatusDto.StatusResponse(
                    userId,
                    requestedStatus
            );
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("잘못된 상태 변경");
        }
    }
    
    @Override
    public List<StatusDto.Summary> findAllUsers() {
        List<StatusDto.Summary> statusDtoList = new ArrayList<>();
        
        userStatusRepository.findAll().forEach(userStatus -> {
            statusDtoList.add(UserStatusMapping.INSTANCE.userStatusToSummary(userStatus));
        });
        
        return statusDtoList;
    }
    
    @Override
    public StatusDto.Summary findById(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("UserStatus", "userId", userId));
            
        return UserStatusMapping.INSTANCE.userStatusToSummary(userStatus);
    }
    
    @Override
    public boolean deleteUserStatus(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("UserStatus", "userId", userId));
            
        boolean success = userStatusRepository.delete(userId);
        return success;
    }
    @Override
    public void setUserOnline(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserStatus", "userId", userId));
        userStatus.setLastSeenAt(ZonedDateTime.now());
        userStatus.setStatus(StatusOperation.ONLINE.ONLINE);

        if (!userStatusRepository.update(userStatus)) {
            throw new RuntimeException("업데이트 실패");
        }
    }

}
