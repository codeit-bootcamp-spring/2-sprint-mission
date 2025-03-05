package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private volatile static JCFUserService instance = null;
    private final Map<UUID, User> userRepository;

    public JCFUserService() {
        this.userRepository = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }


    @Override
    public User saveUser(String name) {
        User user = new User(name);
        userRepository.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        if(userRepository.isEmpty()){
            throw new NoSuchElementException("유저가 없습니다.");
        }

        return userRepository.values().stream().toList();
    }

    @Override
    public List<User> findByName(String name) {
        List<User> users = userRepository.values().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .toList();

        if(users.isEmpty()){
            throw new NoSuchElementException("해당 이름의 유저가 없습니다.");
        }
        return users;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userRepository.get(id));
    }

    @Override
    public void update(UUID id, String name) {
        if(!userRepository.containsKey(id)){
            throw new NoSuchElementException("유저가 없습니다.");
        }
        if(name == null){
            throw new IllegalArgumentException("수정할 이름은 null일 수 없습니다.");
        }

        userRepository.get(id).setName(name);
    }

    @Override
    public void delete(UUID id){
        userRepository.remove(id);
    }
}
