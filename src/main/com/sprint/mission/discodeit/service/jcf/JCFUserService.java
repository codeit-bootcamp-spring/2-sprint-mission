package main.com.sprint.mission.discodeit.service.jcf;

import main.com.sprint.mission.discodeit.entity.User;
import main.com.sprint.mission.discodeit.service.UserService;

import java.util.*;

// UserService interface를 참조하여 기능을 구현한다.
public class JCFUserService implements UserService {
    // user 목록
    private final Map<UUID, User> usermap = new HashMap<>();

    // Create - 생성
    @Override
    public void createUser(String name) {
        // 생성자를 통해 id 생성
        User user = new User(name);
        usermap.put(user.getId(), user);
    }

    // Read - 읽기, 조회
    @Override
    public List<User> getAllUser(){
        List<User> userList = null;
        for(Map.Entry<UUID, User> entry : usermap.entrySet()){
            userList.add(entry.getValue());
        }
        return userList;
    }
    @Override
    public Optional<User> getOneUser(UUID id){
        return Optional.ofNullable(usermap.get(id));
    }
    // Update - 수정

    @Override
    public void updateUser(String newName, UUID id) {
        usermap.get(id).updateUser(newName);
    }

    @Override
    public void deleteUser(UUID id) {
        usermap.remove(id);
    }
}
