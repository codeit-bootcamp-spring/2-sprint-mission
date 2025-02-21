package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.TimeFormatter;
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
        List<User> userList = userRepository.values().stream()
                        .filter(user -> user.getName()
                                .equalsIgnoreCase(name)).toList();

        if(userList.isEmpty()){
            System.out.println("이름이 " + name + " 인 유저가 없습니다.");
        }else {
            System.out.println("[" + name + "]" + " 유저 검색 결과");
            for (User user : userList) {
                System.out.println("유저 ID: " + user.getId());
                System.out.println("유저 이름: " + user.getName());
                System.out.println("유저 생성 일자: " + TimeFormatter.format(user.getCreatedAt()));
                System.out.println("유저 수정 일자: " + TimeFormatter.format(user.getUpdatedAt()));
                System.out.println("----------------------------------");
            }
        }
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
