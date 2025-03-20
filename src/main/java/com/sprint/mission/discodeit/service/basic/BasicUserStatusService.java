package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.UserStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.DTO.UserStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.DTO.UserStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.Status;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatusDto create(CreateUserStatusDto dto) {
        userRepository.findById(dto.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + dto.userId()));
        if (userStatusRepository.findByUserId(dto.userId()).isPresent()) {
            throw new IllegalStateException("A UserStatus for this user already exists: " + dto.userId());
        }

        UserStatus userStatus = new UserStatus(dto.userId(), dto.status());
        return mapToDto(userStatusRepository.save(userStatus));
    }

    @Override
    public UserStatusDto find(UUID id) {
        return userStatusRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found: " + id));
    }

    @Override
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public UserStatusDto update(UpdateUserStatusDto dto) {
        Optional<UserStatus> optionalUserStatus = Optional.ofNullable(
                dto.id() != null ? userStatusRepository.findById(dto.id()).orElse(null) :
                        dto.userId() != null ? userStatusRepository.findByUserId(dto.userId()).orElse(null) :
                                null
        );

        UserStatus existing = optionalUserStatus.orElseThrow(() ->
                new NoSuchElementException("No UserStatus found for given criteria")
        );

        if (dto.lastActivatedAt() != null) {
            existing.updateLastActivatedAt(dto.lastActivatedAt());
        }
        if (dto.status() != null) {
            existing.setUpdatedAt(Instant.now());
            existing.setStatus(dto.status());
        }

        return mapToDto(userStatusRepository.save(existing));
    }

    @Override
    public void delete(UUID id) {
        // 삭제 처리
        userStatusRepository.deleteById(id);
    }

    private UserStatusDto mapToDto(UserStatus userStatus) {
        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastActivatedAt(),
                userStatus.getCurrentOnline() ? Status.ONLINE : Status.OFFLINE
        );
    }
}
