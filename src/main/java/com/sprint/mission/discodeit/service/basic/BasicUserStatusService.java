package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.custom.DuplicateResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UUID create(CreateUserStatusRequest request) {
        UUID userId = request.getUserId();

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new NoSuchElementException("[ERROR] user not found");
        }

        UserStatus check = userStatusRepository.findByUserId(request.getUserId());
        if (check != null) {
            throw new DuplicateResourceException("[ERROR] user status is already exist");
        }

        Instant lastActiveAt = request.getLastActiveAt();

        UserStatus userStatus = new UserStatus(userId, lastActiveAt);
        userStatusRepository.save(userStatus);

        return userStatus.getId();
    }

    @Override
    public UserStatus findByUserStatusId(UUID id) {
        return userStatusRepository.findByUserStatusId(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UUID update(UUID userStatusId, UpdateUserStatusRequest request) {
        Instant newLastActiveAt = request.getNewLastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserStatusId(userStatusId);
        if (userStatus == null) {
            throw new NoSuchElementException("[ERROR] user status not found");
        }
        userStatus.updateLastSeenAt(newLastActiveAt);

        userStatusRepository.save(userStatus);

        return userStatus.getId();
    }


    @Override
    public UUID updateByUserId(UUID userId, UpdateUserStatusRequest request) {
        Instant newLastActiveAt = request.getNewLastActiveAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus == null) {
            throw new NoSuchElementException("[ERROR] user status not found");
        }

        userStatus.updateLastSeenAt(newLastActiveAt);
        userStatusRepository.save(userStatus);

        return userStatus.getId();
    }

    @Override
    public void delete(UUID userId) {
        userStatusRepository.delete(userId);
    }
}
