package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Collections;
import java.util.List;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User create(User user) {
        if (find(user.getName()) != null) {
            System.out.println("등록된 사용자가 존재합니다.");
            return null;
        } else{
            userRepository.save(user);
            System.out.println(user);
            return user;
        }
    }

    @Override
    public User getUser(String name) {
        return find(name);
    }

    private User find(String name) {
        return userRepository.load().stream()
                .filter(user -> user.getName().equals(name))
                .findAny()
                .orElse(null);
    }


    @Override
    public List<User> getAllUser() {
        List<User> userList = userRepository.load();
        if (userList.isEmpty()) {
            System.out.println("전체 조회 결과가 없습니다.");
            return Collections.emptyList();
        }
        return userList;
    }

    @Override
    public User update(String name, String changeName, String changeEmail) {
        User user = find(name);
        if (user == null) {
            System.out.println("사용자가 존재하지 않습니다.");
            return null;
        } else {
            user.update(changeName, changeEmail);
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public void delete(String name) {
        User user = find(name);
        if (user == null) {
            System.out.println("채널이 존재하지 않습니다.");
        } else {
            userRepository.deleteFromFile(user);
        }
    }
}
