package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.*;

public class JCFUserStatusRepository implements UserStatusRepository {
    private final HashMap<UUID, UserStatus> userStatuss = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        userStatuss.put(userStatus.getId(), userStatus);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatuss.get(id));
    }

    @Override
    public Optional<List<UserStatus>> findAll() {
        return Optional.of(new ArrayList<>(userStatuss.values()));
    }

    @Override
    public void update(UserStatus user) {
        save(user);
    }

    @Override
    public void delete(UUID id) {
        userStatuss.remove(id);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatuss.values()
                .stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }
}
