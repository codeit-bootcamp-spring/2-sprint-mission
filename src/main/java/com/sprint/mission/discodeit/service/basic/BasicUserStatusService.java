package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.usertstatus.UpdateUserStatusReqDto;
import com.sprint.mission.discodeit.dto.usertstatus.UserStatusResDto;
import com.sprint.mission.discodeit.dto.usertstatus.CreateUserStatusReqDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResDto create(CreateUserStatusReqDto createUserStatusReqDto) {
        if (!userRepository.existsById(createUserStatusReqDto.userId())) {
            throw new NoSuchElementException("해당하는 user 객체를 찾을 수 없습니다.");
        }

        userStatusRepository.findByUserId(createUserStatusReqDto.userId())
                .ifPresent(userStatus -> {
                    throw new IllegalArgumentException("관련된 userStatus 객체가 이미 존재합니다.");
                });

        UserStatus userStatus = new UserStatus(createUserStatusReqDto.userId(), Instant.now());
        userStatusRepository.save(userStatus);
        return new UserStatusResDto(userStatus.getId(), userStatus.getUserId(), userStatus.isOnline());
    }

    @Override
    public UserStatusResDto find(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 userStatus 찾을 수 없습니다."));
        return new UserStatusResDto(userStatus.getId(), userStatus.getUserId(), userStatus.isOnline());
    }

    @Override
    public List<UserStatusResDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(userStatus ->
                        new UserStatusResDto(userStatus.getId(), userStatus.getUserId(), userStatus.isOnline())).toList();
    }

    @Override
    public UserStatusResDto update(UUID userStatusId, UpdateUserStatusReqDto updateUserStatusReqDto) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 userStatus 객체를 찾을 수 없습니다."));
        userStatus.updateUserStatus(updateUserStatusReqDto.lastOnlineTime(), Instant.now());

        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
        return new UserStatusResDto(updatedUserStatus.getId(), updatedUserStatus.getUserId(), updatedUserStatus.isOnline());
    }

    @Override
    public UserStatusResDto updateByUserId(UUID userId, UpdateUserStatusReqDto updateUserStatusReqDto) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 userStatus 객체를 찾을 수 없습니다."));
        userStatus.updateUserStatus(updateUserStatusReqDto.lastOnlineTime(), Instant.now());

        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
        return new UserStatusResDto(updatedUserStatus.getId(), updatedUserStatus.getUserId(), updatedUserStatus.isOnline());
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw new NoSuchElementException("해당하는 userStatus 객체를 찾을 수 없습니다.");
        }
        userStatusRepository.deleteById(userStatusId);
    }
}
