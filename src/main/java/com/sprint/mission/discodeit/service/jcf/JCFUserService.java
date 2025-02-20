package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.TimeFormatter;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    Map<UUID, User> userRepository = new HashMap<>();

    @Override
    public void saveUser(User user) {
        userRepository.put(user.getId(), user);
    }

    @Override
    public void findAll() {
        List<User> userList = userRepository.values().stream().toList();

        if(userList.isEmpty()){
            System.out.println("등록된 유저가 없습니다.");
        }else {
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
        userRepository.get(id).setUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public void delete(UUID id){
        userRepository.remove(id);
    }
}
