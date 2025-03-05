package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.Factory.CreateChannalFactory;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.file.FileServerRepository;
import com.sprint.mission.discodeit.entity.Container.Channel;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.service.ServerService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileServerService implements ServerService {
    private static FileServerService instance;
    private final Map<UUID, ServerRepository> serverTable = new HashMap<>();

    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "ContainerList.ser");

    private FileServerService() {
    }

    public static FileServerService getInstance() {
        if (instance == null) {
            instance = new FileServerService();
        }
        return instance;
    }

    private ServerRepository getServerRepository(UUID id) {
        ServerRepository serverRepository = serverTable.get(id);
        if (serverRepository == null) {
            ServerRepository repository = new FileServerRepository();
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
    public void addChannel(UUID serverId, String name) {
        ServerRepository serverRepository = getServerRepository(serverId);
        Channel channel = CreateChannalFactory.getInstance().create(name);
        serverRepository.save(channel);
    }

    @Override
    public void addChannel(UUID serverId, Container channel) {
        ServerRepository serverRepository = getServerRepository(serverId);
        serverRepository.save(channel);
    }

    @Override
    public Container getChannel(UUID serverId, String name) {
        ServerRepository serverRepository = getServerRepository(serverId);
        List<Container> containerList = serverRepository.getContainerList();
        for (Container container : containerList) {
            if (container.getName().equals(name)) {
                return container;
            }
        }
        return null;
    }

    @Override
    public void printChannel(UUID serverId) {
        ServerRepository serverRepository = getServerRepository(serverId);
        List<Container> containerList = serverRepository.getContainerList();
        printChannel(containerList);
    }


    private void printChannel(List<Container> list) {
        System.out.println("\n=========채널 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
        System.out.println("=========================\n");
    }

    @Override
    public boolean removeChannel(UUID serverId) {
        ServerRepository serverRepository = getServerRepository(serverId);
        List<Container> containerList = serverRepository.getContainerList();
        if (containerList == null) {
            System.out.println("채널 삭제 실패 : list null값");
            return false;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("삭제할 채널 이름을 입력하시오. : ");
        String targetName = sc.nextLine();

        return removeChannel(containerList,targetName,serverRepository);
    }

    @Override
    public boolean removeChannel(UUID serverId, String targetName) {
        ServerRepository serverRepository = getServerRepository(serverId);
        List<Container> containerList = serverRepository.getContainerList();
        if (containerList == null) {
            System.out.println("채널 삭제 실패 : list null값");
            return false;
        }

        return removeChannel(containerList,targetName,serverRepository);
    }


    private boolean removeChannel(List<Container> list, String targetName, ServerRepository serverRepository) {
        Container targetChannel = list.stream().filter(c -> c.getName().equals(targetName))
                .findFirst().orElse(null);
        if (targetChannel == null) {
            System.out.println("삭제할 채널이 존재하지 않습니다.");
            return false;
        } else {
            list.remove(targetChannel);
            serverRepository.updateContainerList(list);
        }
        return true;
    }

    @Override
    public boolean updateChannel(UUID serverId) {
        ServerRepository serverRepository = getServerRepository(serverId);
        List<Container> containerList = serverRepository.getContainerList();

        Scanner sc = new Scanner(System.in);
        System.out.print("바꿀려고 하는 채널의 이름을 입력하시오. : ");
        String targetName = sc.nextLine();

        return updateChannel(serverId, containerList, targetName);
    }

    @Override
    public boolean updateChannel(UUID serverId, String targetName) {
        ServerRepository serverRepository = getServerRepository(serverId);
        List<Container> containerList = serverRepository.getContainerList();

        Scanner sc = new Scanner(System.in);
        System.out.print("채널 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();

        return updateChannel(serverId, containerList, targetName, replaceName);
    }

    @Override
    public boolean updateChannel(UUID serverId, String targetName, String replaceName) {
        ServerRepository serverRepository = getServerRepository(serverId);
        List<Container> containerList = serverRepository.getContainerList();

        return updateChannel(serverId, containerList, targetName, replaceName);
    }


    private boolean updateChannel(UUID serverId,List<Container> list, String targetName) {
        Scanner sc = new Scanner(System.in);
        System.out.print("채널 이름을 무엇으로 바꾸시겠습니까? : ");
        String replaceName = sc.nextLine();

        return updateChannel(serverId, list, targetName, replaceName);

    }


    private boolean updateChannel(UUID serverId,List<Container> list, String targetName, String replaceName) {
        ServerRepository serverRepository = getServerRepository(serverId);
        Container targetChannel = list.stream().filter(c -> c.getName().equals(targetName))
                .findFirst().orElse(null);
        if (targetChannel != null) {
            for (Container container : list) {
                if (container.getId().equals(targetChannel.getId())) {
                    container.setName(replaceName);
                    serverRepository.updateContainerList(list);
                    return true;
                }
            }
        } else {
            System.out.println("업데이트할 채널이 존재하지 않습니다.");
        }
        return false;
    }


}
