package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class JCFUserService implements UserService {
    // JCF를 활용해 데이터를 저장할 수 있는 필드를 final로 선언&생성자에서 초기화.
    // 싱글톤으로 만들어서 users를 유지할 수 있도록 함.
    //private final Map<String, User> users;
    private final Map<UUID, User> userData;
    private final Map<String, UUID> userNameToId;
    private static final JCFUserService userinstance = new JCFUserService();

    private JCFUserService() {
        this.userData = new HashMap<>();
        this.userNameToId = new HashMap<>();
    }
    public static JCFUserService getInstance() {
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
        return userData.values().toString();
    }

    @Override
    public void registerUser(String userName, String nickName) {
        if(userNameToId.containsKey(userName)){
            System.out.println("존재하는 사용자명입니다.");
        }
        User user = new User(userName, nickName);
        userData.put(user.getUid(), user);
        userNameToId.put(userName, user.getUid());
    }



}
