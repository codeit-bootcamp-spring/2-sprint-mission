package com.sprint.discodeit.repository.file;

import com.sprint.discodeit.domain.entity.UserStatus;
import com.sprint.discodeit.repository.UserStatusRepository;
import com.sprint.discodeit.repository.util.AbstractFileRepository;
import com.sprint.discodeit.repository.util.FilePathUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class BaseUserStatusRepository extends AbstractFileRepository<UserStatus> implements UserStatusRepository {

    protected BaseUserStatusRepository() {
        super(FilePathUtil.STATUS.getPath());
    }


    @Override
    public Optional<UserStatus> findById(String uuId) {
        Map<UUID, UserStatus> statusMap = loadAll();
        return Optional.ofNullable(statusMap.get(UUID.fromString(uuId)));
    }

    @Override
    public List<UserStatus> findByAll() {
        Map<UUID, UserStatus> statusMap = loadAll();
        return statusMap.values().stream().toList();
    }

    public Optional<UserStatus> findUserId(UUID userId) {
        Map<UUID, UserStatus> statusMap = loadAll();
        Optional<UserStatus> status = statusMap.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
        return Optional.ofNullable(status.get());

    }

}
