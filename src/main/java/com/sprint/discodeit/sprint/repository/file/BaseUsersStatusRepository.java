package com.sprint.discodeit.sprint.repository.file;

import com.sprint.discodeit.sprint.domain.entity.users;
import com.sprint.discodeit.sprint.domain.entity.usersStatus;
import com.sprint.discodeit.sprint.repository.util.AbstractFileRepository;
import com.sprint.discodeit.sprint.repository.util.FilePathUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class BaseUsersStatusRepository extends AbstractFileRepository<usersStatus> implements usersStatusRepository {

    protected BaseUsersStatusRepository() {
        super(FilePathUtil.STATUS.getPath());
    }


    @Override
    public Optional<usersStatus> findById(UUID uuId) {
        Map<UUID, usersStatus> statusMap = loadAll();
        return Optional.ofNullable(statusMap.get(uuId));
    }

    @Override
    public List<usersStatus> findByAll() {
        Map<UUID, usersStatus> statusMap = loadAll();
        return statusMap.values().stream().toList();
    }


    public Map<UUID, usersStatus> findByAllAndusers(List<users> usersList) {
        Map<UUID, usersStatus> statusMap = loadAll();
        return usersList.stream()
                .filter(users -> statusMap.containsKey(users.getUsersStatusId()))
                .collect(Collectors.toMap(users::getUsersStatusId, statusMap::get));
    }


    @Override
    public void save(usersStatus usersStatus) {
        Map<UUID, usersStatus> usersStatusMap = loadAll();
        if (usersStatusMap.containsKey(usersStatus.getId())) {
            throw new IllegalArgumentException("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + usersStatus.getId());
        }
        usersStatusMap.put(usersStatus.getId(), usersStatus);
        writeToFile(usersStatusMap);
    }


    @Override
    public void delete(UUID uuId) {
        Map<UUID, usersStatus> usersStatusMap = loadAll();
        usersStatusMap.remove(uuId);
    }
}
