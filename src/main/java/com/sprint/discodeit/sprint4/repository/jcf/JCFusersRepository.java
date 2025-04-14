package com.sprint.discodeit.sprint4.repository.jcf;

import com.sprint.discodeit.sprint5.domain.entity.users;
import com.sprint.discodeit.sprint5.repository.file.usersRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFusersRepository implements usersRepository {

    private final Map<UUID, users> data;

    public JCFusersRepository(Map<UUID, users> data) {
        this.data = data;
    }


    @Override
    public Optional<users> findById(UUID uuId) {
        return Optional.ofNullable(data.get(uuId));
    }

    @Override
    public List<users> findByAll() {
        return data.values().stream().toList();
    }

    @Override
    public void save(users users) {
        data.put(users.getId(), users);
    }

    @Override
    public void delete(UUID uuId) {
        data.remove(uuId);
    }
}
