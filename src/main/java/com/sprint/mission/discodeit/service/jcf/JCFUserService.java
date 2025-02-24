package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    public void findAll() {
        userRepository.values().stream()
                .findFirst()
                .ifPresentOrElse(
                        user -> userRepository.values()
                                .forEach(System.out::println),
                        () -> System.out.println("유저가 없습니다.")
                );
    }

    @Override
    public void findByName(String name) {
        userRepository.values().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println(name + "으로 등록된 유저가 없습니다.")
                );
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userRepository.get(id));
    }

    @Override
    public void update(UUID id, String name) {
        if(!userRepository.containsKey(id)){
            System.out.println("없는 유저 입니다.");
        }
        userRepository.get(id).setName(name);
        userRepository.get(id).setUpdatedAt();
    }

    @Override
    public void delete(UUID id){
        userRepository.remove(id);
    }
}
