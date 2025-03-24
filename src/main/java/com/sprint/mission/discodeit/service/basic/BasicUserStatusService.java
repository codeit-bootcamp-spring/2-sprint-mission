package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResponse create(UserStatusCreateRequest request) {
        if (!userRepository.existsById(request.userId())) {
            throw new NoSuchElementException("User with id " + request.userId() + " not found");
        }

        Optional<UserStatus> existingStatus = userStatusRepository.findByUserId(request.userId());
        if (existingStatus.isPresent()) {
            existingStatus.get().updateStatus();
            userStatusRepository.save(existingStatus.get());
            return new UserStatusResponse(existingStatus.get().getId(), existingStatus.get().getUserId(), existingStatus.get().getUpdatedAt(), existingStatus.get().isOnline());
        }

        UserStatus userStatus = new UserStatus(request.userId());
        userStatusRepository.save(userStatus);
        return new UserStatusResponse(userStatus.getId(), userStatus.getUserId(), userStatus.getUpdatedAt(), userStatus.isOnline());
    }

    @Override
    public Optional<UserStatusResponse> find(UUID id) {
        return userStatusRepository.findById(id).map(status ->
                new UserStatusResponse(status.getId(), status.getUserId(), status.getUpdatedAt(), status.isOnline()));
    }

    @Override
    public List<UserStatusResponse> findAll() {
        return userStatusRepository.findAll().stream()
                .map(status -> new UserStatusResponse(status.getId(), status.getUserId(), status.getUpdatedAt(), status.isOnline()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + request.id() + " not found"));

        userStatus.updateStatus();
        userStatusRepository.save(userStatus);
    }

    @Override
    public void updateByUserId(UserStatusUpdateByUserIdRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(request.userId())
                .orElseThrow(() -> new NoSuchElementException("UserStatus for userId " + request.userId() + " not found"));

        userStatus.updateStatus();
        userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID id) {
        if (userStatusRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("UserStatus with id " + id + " not found");
        }
        userStatusRepository.deleteById(id);
    }
}
