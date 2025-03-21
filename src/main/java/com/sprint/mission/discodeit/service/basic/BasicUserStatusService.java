package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
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
    public UserStatus create(UserStatusDto userStatusDto) {
        if (!userRepository.existsById(userStatusDto.userId())) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다.");
        }

        if (userStatusRepository.existsByUserId(userStatusDto.userId())) {
            throw new IllegalStateException("해당 사용자의 상태 정보가 이미 등록되어 있습니다.");
        }

        Instant lastActiveAt = userStatusDto.lastActiveAt();
        UserStatus userStatus = new UserStatus(userStatusDto.userId(), lastActiveAt, true);

        boolean isOnline = userStatus.isCurrentlyOnline();

        userStatus.update(lastActiveAt, isOnline);

        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findById(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자의 상태 정보를 찾을 수 없습니다."));
   }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll().stream().toList();
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusDto userStatusDto) {
        UserStatus userStatusExists = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자의 상태 정보를 찾을 수 없습니다."));

        Instant lastActiveAt = userStatusDto.lastActiveAt();
        boolean isOnline = userStatusExists.isCurrentlyOnline();

        userStatusExists.update(lastActiveAt, isOnline);

        return userStatusRepository.save(userStatusExists);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusDto userStatusDto) {
        UserStatus userStatusExists = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));

        Instant lastActiveAt = userStatusDto.lastActiveAt();
        boolean isOnline = userStatusExists.isCurrentlyOnline();

        userStatusExists.update(lastActiveAt, isOnline);

        return userStatusRepository.save(userStatusExists);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw new NoSuchElementException("해당 사용자의 상태 정보를 찾을 수 없습니다.");
        }

        userStatusRepository.deleteById(userStatusId);
    }
}
