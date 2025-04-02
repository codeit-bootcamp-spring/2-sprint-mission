package com.sprint.discodeit.sprint5.repository.file;

import com.sprint.discodeit.sprint5.domain.entity.User;
import com.sprint.discodeit.sprint5.domain.entity.UserStatus;
import com.sprint.discodeit.sprint5.repository.util.AbstractFileRepository;
import com.sprint.discodeit.sprint5.repository.util.FilePathUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class BaseUserStatusRepository extends AbstractFileRepository<UserStatus> implements UserStatusRepository {

    protected BaseUserStatusRepository() {
        super(FilePathUtil.STATUS.getPath());
    }


    @Override
    public Optional<UserStatus> findById(UUID uuId) {
        Map<UUID, UserStatus> statusMap = loadAll();
        return Optional.ofNullable(statusMap.get(uuId));
    }

    @Override
    public List<UserStatus> findByAll() {
        Map<UUID, UserStatus> statusMap = loadAll();
        return statusMap.values().stream().toList();
    }


    public Map<UUID, UserStatus> findByAllAndUser(List<User> userList) {
        Map<UUID, UserStatus> statusMap = loadAll();
        return userList.stream()
                .filter(user -> statusMap.containsKey(user.getUserStatusId()))
                .collect(Collectors.toMap(User::getUserStatusId, statusMap::get));
    }


    @Override
    public void save(UserStatus userStatus) {
        Map<UUID, UserStatus> userStatusMap = loadAll();
        if (userStatusMap.containsKey(userStatus.getId())) {
            throw new IllegalArgumentException("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + userStatus.getId());
        }
        userStatusMap.put(userStatus.getId(), userStatus);
        writeToFile(userStatusMap);
    }


    @Override
    public void delete(UUID uuId) {
        Map<UUID, UserStatus> userStatusMap = loadAll();
        userStatusMap.remove(uuId);
    }
}
