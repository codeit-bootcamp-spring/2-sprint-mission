package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.composit.CategoryAndChannel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.ServerService;

import java.util.LinkedList;

public class JCFServerService implements ServerService {
    private static JCFServerService instance;
    private JCFServerService(){
    }

    public static JCFServerService getInstance() {
        if (instance == null) {
            instance = new JCFServerService();
        }
        return instance;
    }

    @Override
    public void print() {

    }

    @Override
    public void addChannel(String name) {

    }

    @Override
    public void update(String targetName, String replaceName) {

    }

    @Override
    public void remove(String targetName) {

    }
}
