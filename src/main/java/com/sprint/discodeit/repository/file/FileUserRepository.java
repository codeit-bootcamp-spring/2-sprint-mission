package com.sprint.discodeit.repository.file;

import com.sprint.discodeit.entity.User;
import com.sprint.discodeit.repository.util.AbstractFileRepository;
import com.sprint.discodeit.repository.UserRepository;
import com.sprint.discodeit.repository.util.FilePathUtil;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {


    public FileUserRepository() {
        super(FilePathUtil.USERS.getPath());
    }

    @Override
    public User findById(String userId) {
        Map<UUID, User> users = loadAll();
        return  Optional.ofNullable(users.get(UUID.fromString(userId.toString())))
                .orElseThrow(() -> new NoSuchElementException(userId + " 없는 회원 입니다"));
    }


    @Override
    public List<User> findByAll() {
        Map<UUID, User> users = loadAll();
        return users.values().stream().toList();
    }

    @Override
    public void save(User user) {
        Map<UUID, User> users = loadAll();
        if (users.containsKey(user.getId())) {
            System.out.println("[DEBUG] 동일한 UUID의 데이터가 이미 존재하므로 추가하지 않음: " + user.getId());
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
}
