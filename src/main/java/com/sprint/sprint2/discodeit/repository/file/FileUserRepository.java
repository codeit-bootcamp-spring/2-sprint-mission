package com.sprint.sprint2.discodeit.repository.file;

import com.sprint.sprint2.discodeit.entity.User;
import com.sprint.sprint2.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {

    private static final String FILE_PATH = "users.ser";

    public FileUserRepository() {
        super(FILE_PATH);
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
