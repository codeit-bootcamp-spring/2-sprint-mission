package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatus create(UserStatusCreateRequestDto userStatusCreateRequestDto) {
        User user = userRepository.findById(userStatusCreateRequestDto.userId())
                    .orElseThrow(() -> new NoSuchElementException("해당 유저 없음"));

        if (userStatusRepository.findByUserId(userStatusCreateRequestDto.userId()).isPresent()) {
            throw new IllegalArgumentException("해당 유저의 userStatus 이미 존재");
        }

        UserStatus userStatus = new UserStatus(userStatusCreateRequestDto.userId());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 유저 상태 없음"));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        UserStatus userStatus = userStatusRepository.findById(userStatusUpdateRequestDto.id())
                .orElseThrow(() -> new NoSuchElementException("해당 유저 상태 없음"));

        userStatus.updateLastLoginAt();
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저 상태 없음"));
        userStatus.updateLastLoginAt();
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!userStatusRepository.existsById(id)) {
            throw new  NoSuchElementException("해당 유저 상태 없음");
        }
        userStatusRepository.deleteById(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저 상태 없음"));
        userStatusRepository.deleteById(userStatus.getId());
    }
}
