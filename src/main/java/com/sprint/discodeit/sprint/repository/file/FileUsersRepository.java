package com.sprint.discodeit.sprint.repository.file;

import com.sprint.discodeit.sprint.domain.entity.users;
import com.sprint.discodeit.sprint.repository.util.AbstractFileRepository;
import com.sprint.discodeit.sprint.repository.util.FilePathUtil;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class FileUsersRepository extends AbstractFileRepository<users> implements usersRepository {


    public FileUsersRepository() {
        super(FilePathUtil.usersS.getPath());
    }

    @Override
    public Optional<users> findById(UUID usersId) {
        Map<UUID, users> userss = loadAll();
        return Optional.ofNullable(userss.get(usersId));
    }


    @Override
    public List<users> findByAll() {
        Map<UUID, users> userss = loadAll();
        return userss.values().stream()
                .filter(users -> !users.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public void save(users users) {
        Map<UUID, users> userss = loadAll();
        if (userss.containsKey(users.getId())) {
            throw new IllegalArgumentException("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + users.getId());
        } else {
            userss.put(users.getId(), users);
            writeToFile(userss);
        }
    }

    @Override
    public void delete(UUID usersId) {
        Map<UUID, users> userss = loadAll();
        userss.remove(usersId);
        writeToFile(userss);
    }

    public Optional<users> findByusersname(String usersname) {
        Map<UUID, users> userss = loadAll();
        return userss.values().stream().filter(users -> Objects.toString(users.getUsersname(), "").equals(usersname)).findFirst();
    }

    public Optional<users> findByEmail(String email) {
        Map<UUID, users> userss = loadAll();
        return userss.values().stream().filter(users -> Objects.toString(users.getEmail(), "").equals(email)).findFirst();
    }

    public Optional<users> findByPassword(String password) {
        Map<UUID, users> userss = loadAll();
        return userss.values().stream().filter(users -> users.getPassword().equals(password)).findFirst();
    }

    public Optional<users> findByStatusId(UUID statusId) {
        Map<UUID, users> userss = loadAll();
        return userss.values().stream().filter(users -> users.getUsersStatusId().equals(statusId)).findFirst();
    }

    public UUID findByusersIdAndStatusId(UUID usersId) {
        Map<UUID, users> userss = loadAll();
        Optional<users> usersById = userss.values().stream()
                .filter(users -> users.getUsersStatusId().equals(usersId))
                .findFirst();
        return usersById.get().getUsersStatusId();
    }
}
