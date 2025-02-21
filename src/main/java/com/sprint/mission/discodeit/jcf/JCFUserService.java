package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


public class JCFUserService implements UserService {
    // JCF를 활용해 데이터를 저장할 수 있는 필드를 final로 선언&생성자에서 초기화.
    // 싱글톤으로 만들어서 users를 유지할 수 있도록 함.
    //private final Map<String, User> users;
    private final Map<UUID, User> userData;
    private final Map<String, UUID> userNameToId;
    private static JCFUserService userinstance;

    private JCFUserService() {
        this.userData = new HashMap<>();
        this.userNameToId = new HashMap<>();
    }
    public static JCFUserService getInstance() {
        if(userinstance == null) {
            userinstance = new JCFUserService();
        }
        return userinstance;
    }

    @Override
    public String getUser(String userName) {
        if(!(userNameToId.containsKey(userName))){
            return "존재하지 않는 사용자명입니다.";
        }
        return userData.get(userNameToId.get(userName)).toString();
    }

    @Override
    public String getAllUsers(){
        return userData.values().stream()
                .map(User::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<User> getUpdatedUsers(){
        return userData.values().stream()
                .filter(entry -> entry.getUserUpdateAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void registerUser(String userName, String nickName) {
        if(userNameToId.containsKey(userName)){
            System.out.println("존재하는 사용자명입니다.");
            return;
        }
        User user = new User(userName, nickName);
        UUID uid = user.getUid();
        userData.put(uid, user);
        userNameToId.put(userName, uid);
    }

    @Override
    public boolean userNameExists(String userName) {
        return userNameToId.containsKey(userName);
    }

    @Override
    public void updateName(String oldUserName, String newUserName, String newNickName) {
        if(!(userNameToId.containsKey(oldUserName))){
             System.out.println("존재하지 않는 사용자입니다.");
             return;
        }
        UUID uid = userNameToId.get(oldUserName);
        User user = userData.get(uid);
        user.userUpdate(newUserName, newNickName);
        userNameToId.remove(oldUserName);
        userNameToId.put(newUserName, uid);
    }

    @Override
    public String deleteUser(String userName) {
        if(!(userNameToId.containsKey(userName))){
            return "존재하지 않는 사용자입니다.";
        }
        UUID uid = userNameToId.get(userName);
        userData.remove(uid);
        userNameToId.remove(userName);
        return getAllUsers();
    }

}
