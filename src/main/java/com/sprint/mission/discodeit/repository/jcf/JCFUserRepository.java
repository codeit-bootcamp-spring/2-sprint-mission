package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private volatile static JCFUserRepository instance = null;
    private final Map<UUID, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    public static JCFUserRepository getInstance() {
        if(instance == null){
            synchronized (JCFUserRepository.class){
                if(instance == null){
                    instance = new JCFUserRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User findById(UUID id) {
        return Optional.ofNullable(data.get(id)).orElseThrow(()->new NoSuchElementException("User not found"));
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
