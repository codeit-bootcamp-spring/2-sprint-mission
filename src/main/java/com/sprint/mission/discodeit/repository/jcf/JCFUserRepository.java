package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)
// matchIfMissing = true 의 의미는 type지정이 안되어있을 경우
// 자동으로 file 대신 jcf를 실행한다는 의미
// file은 type 지정을 무조건 file로 해야만 실행된다
public class JCFUserRepository implements UserRepository {
    private Map<UUID, User> users = new HashMap<>();

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll(){
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public boolean existsById(UUID id) {
        return users.containsKey(id);
    }

    @Override
    public User save(User user) {
        if(user.getId() == null){
            user.setId(UUID.randomUUID());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(UUID id){
        users.remove(id);
    }
}
