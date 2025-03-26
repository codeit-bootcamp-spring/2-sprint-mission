package com.sprint.discodeit.repository.file;

import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.repository.util.AbstractFileRepository;
import com.sprint.discodeit.repository.UserRepository;
import com.sprint.discodeit.repository.util.FilePathUtil;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {


    public FileUserRepository() {
        super(FilePathUtil.USERS.getPath());
    }

    @Override
    public Optional<User> findById(UUID userId) {
        Map<UUID, User> users = loadAll();
        return Optional.ofNullable(users.get(userId));
    }


    @Override
    public List<User> findByAll() {
        Map<UUID, User> users = loadAll();
        return users.values().stream()
                .filter(user -> !user.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        Map<UUID, User> users = loadAll();
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + user.getId());
        } else {
            users.put(user.getId(), user);
            writeToFile(users);
        }
    }

    @Override
    public void delete(UUID userId) {
        Map<UUID, User> users = loadAll();
        users.remove(userId);
        writeToFile(users);
    }

    public Optional<User> findByUsername(String username) {
        Map<UUID, User> users = loadAll();
        return users.values().stream().filter(user -> Objects.toString(user.getUsername(), "").equals(username)).findFirst();
    }

    public Optional<User> findByEmail(String email) {
        Map<UUID, User> users = loadAll();
        return users.values().stream().filter(user -> Objects.toString(user.getEmail(), "").equals(email)).findFirst();
    }

    public Optional<User> findByPassword(String password) {
        Map<UUID, User> users = loadAll();
        return users.values().stream().filter(user -> user.getPassword().equals(password)).findFirst();
    }

    public Optional<User> findByStatusId(UUID statusId) {
        Map<UUID, User> users = loadAll();
        return users.values().stream().filter(user -> user.getUserStatusId().equals(statusId)).findFirst();
    }

    public UUID findByUserIdAndStatusId(UUID userId) {
        Map<UUID, User> users = loadAll();
        Optional<User> userById = users.values().stream()
                .filter(user -> user.getUserStatusId().equals(userId))
                .findFirst();
        return userById.get().getUserStatusId();
    }
}
