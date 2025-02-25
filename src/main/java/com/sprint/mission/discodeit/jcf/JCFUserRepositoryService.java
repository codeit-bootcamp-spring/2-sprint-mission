package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.service.RepositoryService;

import java.util.List;

public class JCFUserRepositoryService implements RepositoryService<User, Server> {
    private static JCFUserRepositoryService instance;

    private JCFUserRepositoryService() {
    }

    public static JCFUserRepositoryService getInstance() {
        if (instance == null) {
            instance = new JCFUserRepositoryService();
        }
        return instance;
    }

    @Override
    public void add(User user, Server server) {
        UserRepository userRepository = user.getUserRepository();
        List<Server> severList = userRepository.getSeverList();
        severList.add(server);
        System.out.println("서버 추가 성공");
    }

    @Override
    public void remove(User user, Server server) {

    }

    @Override
    public void print(User user) {
        UserRepository userRepository = user.getUserRepository();
        List<Server> severList = userRepository.getSeverList();

        for (Server server : severList) {
            System.out.println(server.getName());
        }
    }

    @Override
    public void search(User user) {

    }

    @Override
    public void update(User user) {

    }
}
