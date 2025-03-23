package com.sprint.mission.discodeit.basic.serviceimpl;

import com.sprint.mission.discodeit.dto.StatusDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapping.UserMapping;
import com.sprint.mission.discodeit.mapping.UserStatusMapping;
import com.sprint.mission.discodeit.service.UserRepository;
import com.sprint.mission.discodeit.service.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    @Autowired
    public BasicUserStatusService(
            @Qualifier("basicUserStatusRepository") UserStatusRepository userStatusRepository,
            @Qualifier("basicUserRepository") UserRepository userRepository) {
        this.userStatusRepository = userStatusRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StatusDto.Summary createUserStatus(StatusDto.Create statusDto) {
        // 사용자 존재 확인
        userRepository.findByUser(statusDto.getUserid())
            .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다: " + statusDto.getUserid()));
        
        // 이미 존재하는지 확인
        userStatusRepository.findById(statusDto.getUserid())
            .ifPresent(existing -> {
                throw new IllegalStateException("이미 같은 사용자에 대한 상태가 존재합니다");
            });
        
        UserStatus userStatus = new UserStatus(statusDto.getUserid());
        userStatusRepository.register(userStatus);

        return UserStatusMapping.INSTANCE.userStatusToSummary(userStatus);
    }

    @Override
    public UserDto.Summary updateByUserId(@NotBlank UUID userId) {
        User user = userRepository.findByUser(userId)
            .orElseThrow(() -> new NoSuchElementException("User not found"));
        
        UserStatus userStatus = userStatusRepository.findById(user.getId())
            .orElseThrow(() -> new NoSuchElementException("사용자 상태를 찾을 수 없습니다"));
        
        // 사용자의 마지막 접속 시간 업데이트
        userStatus.updateLastTime();
        userStatusRepository.update(userStatus);
        
        // 사용자 업데이트 시간 갱신
        user.setUpdateAt();
        userRepository.updateUser(user);
        
        return UserMapping.INSTANCE.userToSummary(user, userStatus);
    }
    
    @Override
    public StatusDto.Summary update(StatusDto.Summary statusDto) {
        UserStatus userStatus = userStatusRepository.findById(statusDto.getUserid())
            .orElseThrow(() -> new NoSuchElementException("사용자 상태를 찾을 수 없습니다"));
        
        userStatus.setLastSeenAt(statusDto.getLastSeenAt());
        userStatusRepository.update(userStatus);

        return UserStatusMapping.INSTANCE.userStatusToSummary(userStatus);
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
        UserStatus userStatus = userStatusRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("사용자 상태를 찾을 수 없습니다"));
            
        return UserStatusMapping.INSTANCE.userStatusToSummary(userStatus);
    }
    
    @Override
    public StatusDto.ResponseDelete deleteUserStatus(UUID userId) {
        UserStatus userStatus = userStatusRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("사용자 상태를 찾을 수 없습니다"));
            
        boolean success = userStatusRepository.delete(userId);
        String message = success ? "삭제 성공" : "삭제 실패";
        
        return UserStatusMapping.INSTANCE.userStatusToResponse(userStatus, message);
    }
    

    public boolean isUserOnline(UUID userId) {
        UserStatus userStatus = userStatusRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("사용자 상태를 찾을 수 없습니다"));
            
        if (userStatus.getLastSeenAt() == null) return false;
        
        return userStatus.isOnline();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Optional<UserStatus> userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus.isPresent()) {
            userStatusRepository.delete(userStatus.get().getId());
        }
    }
}
