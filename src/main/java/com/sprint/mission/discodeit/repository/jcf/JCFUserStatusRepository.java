package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusRepository = new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusRepository.put(userStatus.getId(), userStatus);

        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatusRepository.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusRepository.values()
                .stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.values()
                .stream()
                .toList();
    }

    @Override
    public UserStatus update(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.get(userStatusId);
        userStatus.updateLastLoginAt();

        return userStatus;
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.remove(id);
    }
}
