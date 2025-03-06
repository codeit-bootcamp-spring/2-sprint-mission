package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();
    // UUID를 키로 사용하여 User 객체를 저장 / 일반적으로 키는 String UUID Integer 타입(변하지 않는 값)

    @Override
    public void create(User entity) {
        data.put(entity.getId(), entity);
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(data.get(id));
        // data.get(id) == null -> Optional.empty()생성
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
        // data.values() 값(User 객체들)을 Collection 타입으로 반환
    }

    @Override
    public void update(UUID id, User entity) {
        if (data.containsKey(id)) {
            entity.update();
            data.put(id, entity);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
