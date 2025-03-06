package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Factory.CreateChannalFactory;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFServerRepository;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.service.ServerService;

import java.util.*;

public class JCFServerService implements ServerService {
    private static volatile JCFServerService instance;
    private final Map<UUID, ServerRepository> serverTable = new HashMap<>();

    private JCFServerService() {
    }

    public static JCFServerService getInstance() {
        if (instance == null) {
            synchronized (JCFServerService.class) {
                if (instance == null) {
                    instance = new JCFServerService();
                }
            }
        }
        return instance;
    }

    private ServerRepository getServerRepository(UUID id) {
        ServerRepository JCFServerRepository = serverTable.get(id);
        if (JCFServerRepository == null) {
            ServerRepository repository = new JCFServerRepository();
            serverTable.put(id, repository);
            JCFServerRepository = repository;
        }
        return JCFServerRepository;
    }

    @Override
    public Container createChannel(String name) {
        return CreateChannalFactory.getInstance().create(name);
    }

    @Override
    public void addChannel(UUID id, String name) {
        ServerRepository serverRepository = getServerRepository(id);
        Container channel = createChannel(name);
        serverRepository.save(channel);

        //로그
        System.out.println(channel.getName() + " 채널 추가 성공");
    }

    @Override
    public void addChannel(UUID id, Container channel) {
        ServerRepository JCFServerRepository = getServerRepository(id);
        List<Container> list = JCFServerRepository.getContainerList();
        list.add(channel);
        //로그
        System.out.println(channel.getName() + " 채널 추가 성공");
    }

    @Override
    public Container getChannel(UUID id, String name) {
        ServerRepository JCFServerRepository = getServerRepository(id);
        List<Container> list = JCFServerRepository.getContainerList();
        for (Container item : list) {
            if (item.getName().equals(name)) {
                //로그
                System.out.println(item.getName() + " 이(가) 반환됩니다.");
                if (item.getClass() == Container.class) {
                    return item;
                }
            }
        }
        //로그
        System.out.println("존재하지 않습니다.");
        return null;
    }

    @Override
    public void printChannel(UUID id) {
        ServerRepository JCFServerRepository = getServerRepository(id);
        List<Container> list = JCFServerRepository.getContainerList();
        printChannel(list);
    }


    private void printChannel(List<Container> list) {
        System.out.println("\n=========채널 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
        System.out.println("=========================\n");
    }

    @Override
    public boolean removeChannel(UUID id) {
        ServerRepository JCFServerRepository = getServerRepository(id);
        List<Container> list = JCFServerRepository.getContainerList();
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 채널 이름을 입력하시오. : ");
        String targetName = sc.nextLine();
        return removeChannel(list, targetName);
    }

    @Override
    public boolean removeChannel(UUID id, String targetName) {
        ServerRepository JCFServerRepository = getServerRepository(id);
        List<Container> list = JCFServerRepository.getContainerList();
        return removeChannel(list, targetName);
    }


    private boolean removeChannel(List<Container> list, String targetName) {
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
    public boolean updateChannel(UUID id) {
        ServerRepository JCFServerRepository = getServerRepository(id);
        List<Container> list = JCFServerRepository.getContainerList();
        Scanner sc = new Scanner(System.in);
        System.out.print("바꿀려고 하는 채널의 이름을 입력하시오. : ");
        String targetName = sc.nextLine();
        return updateChannel(list, targetName);
    }

    @Override
    public boolean updateChannel(UUID id, String targetName) {
        ServerRepository JCFServerRepository = getServerRepository(id);
        List<Container> list = JCFServerRepository.getContainerList();
        Scanner sc = new Scanner(System.in);
        System.out.print("채널 이름을 무엇으로 바꾸시겠습니까? ");
        String replaceName = sc.nextLine();
        return updateChannel(list, targetName, replaceName);
    }

    @Override
    public boolean updateChannel(UUID id, String targetName, String replaceName) {
        ServerRepository JCFServerRepository = getServerRepository(id);
        List<Container> list = JCFServerRepository.getContainerList();
        return updateChannel(list, targetName, replaceName);
    }


    private boolean updateChannel(List<Container> list, String targetName) {
        Scanner sc = new Scanner(System.in);
        System.out.print("채널 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();
        return updateChannel(list, targetName, replaceName);
    }


    private boolean updateChannel(List<Container> list, String targetName, String replaceName) {
        for (Container item : list) {
            if (item.getName().equals(targetName)) {
                item.setName(replaceName);
                //로그
                System.out.println(targetName + " 이름이 " + item.getName() + " 이(가) 됩니다.");
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }
}
