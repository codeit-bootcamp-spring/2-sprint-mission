package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(value = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileUserStatusRepository extends AbstractFileRepository<Map<UUID, UserStatus>> implements UserStatusRepository {

    private Map<UUID, UserStatus> data;

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory}") String directory) {
        super(directory, UserStatus.class.getSimpleName()+".ser");
        this.data = loadData();
    }

    @Override
    protected Map<UUID, UserStatus> getEmptyData() {
        return new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);
        saveData(data);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        saveData(data);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        List<UUID> keysToRemove = data.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .map(UserStatus::getId)
                .toList();
        keysToRemove.forEach(data::remove);
        saveData(data);
    }
}
