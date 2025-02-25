package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Container;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.RepositoryService;

public class JCFCategoryRepository implements RepositoryService<Server, Container> {
    private static JCFCategoryRepository instance;

    private JCFCategoryRepository() {
    }

    public static JCFCategoryRepository getInstance() {
        if (instance == null) {
            instance = new JCFCategoryRepository();
        }
        return instance;
    }

    @Override
    public void add(Server server, Container container) {

    }

    @Override
    public void remove(Server server, Container container) {

    }

    @Override
    public void print(Server server) {

    }

    @Override
    public void search(Server server) {

    }

    @Override
    public void update(Server server) {

    }
}
