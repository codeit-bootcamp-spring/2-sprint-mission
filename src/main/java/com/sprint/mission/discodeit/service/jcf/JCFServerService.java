package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.service.ServerService;

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
