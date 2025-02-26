package com.sprint.mission.discodeit.FrontEnd;

import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;

public class DiscordRepository {
    private static DiscordRepository instance;
    List<User> list;

    private DiscordRepository( ) {
        list = new ArrayList<>();
    }

    public static DiscordRepository getInstance() {
        if (instance == null) {
            instance = new DiscordRepository();
        }
        return instance;
    }

    public List<User> getList() {
        return list;
    }
}
