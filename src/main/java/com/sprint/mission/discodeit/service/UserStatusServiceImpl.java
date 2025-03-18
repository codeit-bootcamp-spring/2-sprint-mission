package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequestDTO;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatusServiceImpl(UserStatusRepository userStatusRepository, UserRepository userRepository) {
        this.userStatusRepository = userStatusRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserStatus create(UserStatusCreateRequestDTO dto) {
        if (!userRepository.existsById(dto.getUserId())) {
            throw new NoSuchElementException("User not found with id " + dto.getUserId());
        }
        if (userStatusRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalArgumentException("UserStatus already exists for user " + dto.getUserId());
        }

        UserStatus userStatus = new UserStatus(UUID.randomUUID(), dto.getUserId(), dto.getStatus(), Instant.now());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public Optional<UserStatus> find(UUID id) {
        return userStatusRepository.findById(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateRequestDTO dto) {
        UserStatus userStatus = userStatusRepository.findById(dto.getId())
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found with id " + dto.getId()));

        userStatus.setStatus(dto.getStatus());  // ✅ 이제 정상적으로 status 변경 가능
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!userStatusRepository.existsById(id)) {
            throw new NoSuchElementException("UserStatus not found with id " + id);
        }
        userStatusRepository.deleteById(id);
    }
}