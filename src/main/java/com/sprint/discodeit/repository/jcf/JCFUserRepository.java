package com.sprint.discodeit.repository.jcf;

import com.sprint.discodeit.domain.entity.User;
import com.sprint.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data;

    public JCFUserRepository(Map<UUID, User> data) {
        this.data = data;
    }


    @Override
    public Optional<User> findById(String uuId) {
        return Optional.ofNullable(data.get(UUID.fromString(uuId)));
    }

    @Override
    public List<User> findByAll() {
        return data.values().stream().toList();
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public void delete(UUID uuId) {
        data.remove(uuId);
    }
}
