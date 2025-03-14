package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(String name, String email, String password) {
        User user = new User(name, email, password);
        Optional<User> userList = userRepository.load().stream()
                .filter(u -> u.getName().equals(name))
                .findAny();
        if (userList.isPresent()) {
            throw new IllegalArgumentException("등록된 사용자가 존재합니다.");
        } else{
            userRepository.save(user);
            System.out.println(user);
            return user;
        }
    }


    @Override
    public User getUser(UUID userId) {
        Optional<User> user = userRepository.load().stream()
                .filter(u -> u.getId().equals(userId))
                .findAny();
        return user.orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));
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
    public User update(UUID userId, String changeName, String changeEmail, String changePassword) {
        User user = getUser(userId);
        user.update(changeName, changeEmail, changePassword);
        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        User user = getUser(userId);
        userRepository.remove(user);
    }
}
