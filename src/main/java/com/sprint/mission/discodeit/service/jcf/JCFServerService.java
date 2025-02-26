package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Factory.CreateChannalFactory;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.impl.LinkedListServerRepository;
import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.service.ServerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFServerService implements ServerService {
    private static JCFServerService instance;
    private final Map<UUID, ServerRepository> serverTable = new HashMap<>();

    private JCFServerService() {
    }

    public static JCFServerService getInstance() {
        if (instance == null) {
            instance = new JCFServerService();
        }
        return instance;
    }

    private ServerRepository getServerRepository(UUID id) {
        ServerRepository serverRepository = serverTable.get(id);
        if (serverRepository == null) {
            LinkedListServerRepository repository = new LinkedListServerRepository();
            serverTable.put(id, repository);
            serverRepository = repository;
        }
        return serverRepository;
    }

    @Override
    public Channel createChannel(String name) {
        return CreateChannalFactory.getInstance().create(name);
    }

    @Override
    public void addChannel(UUID id, String name) {
        ServerRepository serverRepository = getServerRepository(id);
        Channel channel = createChannel(name);
        serverRepository.add(channel);

        //로그
        System.out.println(channel.getName() + " 채널 추가 성공");
    }

    @Override
    public void addChannel(UUID id, Channel channel) {
        ServerRepository serverRepository = getServerRepository(id);
        List<Container> list = serverRepository.getList();
        list.add(channel);
        //로그
        System.out.println(channel.getName() + " 채널 추가 성공");
    }

    @Override
    public void printChannel(UUID id) {
        ServerRepository serverRepository = getServerRepository(id);
        List<Container> list = serverRepository.getList();
        printChannel(list);
    }

    @Override
    public void printChannel(List<Container> list) {
        System.out.println("=========채널 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
        System.out.println("=========================");
    }

    @Override
    public boolean removeChannel(UUID id, String targetName) {
        ServerRepository serverRepository = getServerRepository(id);
        List<Container> list = serverRepository.getList();
        for (Container item : list) {
            if (item.getName().equals(targetName)) {
                //로그
                System.out.println(item.getName() + " 이(가) 삭제됩니다.");
                list.remove(item);
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }

    @Override
    public boolean updateChannel(UUID id, String targetName, String replaceName) {
        ServerRepository serverRepository = getServerRepository(id);
        List<Container> list = serverRepository.getList();
        for (Container item : list) {
            if (item.getName().equals(targetName)) {
                item.setName(replaceName);
                //로그
                System.out.println(targetName+" 이름이 " + item.getName() + " 이(가) 됩니다.");
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }
}
