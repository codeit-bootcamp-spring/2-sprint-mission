package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequestDto dto) {
        User user = Optional.ofNullable(userRepository.findById(dto.getUserId()))
                .orElseThrow(() -> new NoSuchElementException("User with id " + dto.getUserId() + " not found"));

        if(userStatusRepository.findAll().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(user.getId()))){
            throw new IllegalArgumentException("관련된 객체가 이미 존재합니다.");
        }

        UserStatus userStatus = new UserStatus(dto.getUserId(), dto.getActivatedAt());
        userStatusRepository.save(userStatus);

        return userStatus;
    }

    @Override
    public UserStatus findById(UUID userStatusId){
        return Optional.ofNullable(userStatusRepository.findById(userStatusId))
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    }

    public List<UserStatus> findAll(){
        return userStatusRepository.findAll();
    }

    public UserStatus update(UserStatusUpdateRequestDto dto){
        UserStatus userStatus = userStatusRepository.findById(dto.getId());
        if (userStatus == null) {
            throw new NoSuchElementException("UserStatus with id " + dto.getId() + " not found");
        }

        return userStatusRepository.update(dto);
    }

    public UserStatus updateByUserId(UUID userId, Instant newActivatedAt){
        UserStatus userStatus = userStatusRepository.findAll().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("UserStatus with userid " + userId + " not found"));

        return userStatusRepository.update(new UserStatusUpdateRequestDto(userStatus.getId(), newActivatedAt));
    }

    public void delete(UUID userStatusId){
        userStatusRepository.delete(userStatusId);
    }
}
