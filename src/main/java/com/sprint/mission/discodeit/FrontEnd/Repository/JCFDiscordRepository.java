package com.sprint.mission.discodeit.FrontEnd.Repository;

import com.sprint.mission.discodeit.FrontEnd.DiscordRepository;
import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;

public class JCFDiscordRepository implements DiscordRepository {
    private static JCFDiscordRepository instance;
    List<User> list;

    private JCFDiscordRepository( ) {
        list = new ArrayList<>();
    }

    public static JCFDiscordRepository getInstance() {
        if (instance == null) {
            instance = new JCFDiscordRepository();
        }
        return instance;
    }

    public List<User> getList() {
        return list;
    }

    @Override
    public void register(User user) {
        list.add(user);
    }

    @Override
    public List<User> getUserList() {
        return list;
    }

    @Override
    public void updateUserList(List<User> userList) {
        this.list = userList;
    }
}
