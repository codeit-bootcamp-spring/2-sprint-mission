package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


public class JCFUserService implements UserService {
    private final Map<UUID, User> userData;
    private final Map<String, UUID> userNameToId;
    private static JCFUserService userinstance;
    private JCFChannelService channelService;

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

    public void initializeServices(JCFChannelService channelService) {
        this.channelService = channelService;
    }

    public void containsUserNameToId(String userName){
        if(!(userNameToId.containsKey(userName))){
            throw new IllegalArgumentException("존재하지 않는 사용자명입니다.");
        }
    }

    @Override
    public User getUser(String userName) {
        containsUserNameToId(userName);
        return userData.get(userNameToId.get(userName));
    }

    @Override
    public List<User> getAllUsers(){
        if(userData.isEmpty()){
            throw new IllegalArgumentException("데이터가 존재하지 않습니다.");
        }
        return new ArrayList<>(userData.values());
    }

    public List<User> getUpdatedUsers(){
        return userData.values().stream()
                .filter(entry -> entry.getUserUpdateAt() != null)
                .collect(Collectors.toList());
    }

    @Override
    public void registerUser(String userName, String nickName) {
        if(userNameToId.containsKey(userName)){
            throw new IllegalArgumentException("존재하는 사용자명입니다.");
        }
        User user = new User(userName, nickName);
        UUID uid = user.getUid();
        userData.put(uid, user);
        userNameToId.put(userName, uid);
    }

    @Override
    public void updateName(String oldUserName, String newUserName, String newNickName) {
        containsUserNameToId(oldUserName);
        UUID uid = userNameToId.get(oldUserName);
        User user = userData.get(uid);
        user.userUpdate(newUserName, newNickName);
        userNameToId.remove(oldUserName);
        userNameToId.put(newUserName, uid);
    }

    @Override
    public void deleteUser(String userName) {
        containsUserNameToId(userName);
        UUID uid = userNameToId.get(userName);
        userData.remove(uid);
        userNameToId.remove(userName);
        channelService.deleteChannelByuid(uid);
    }
}
