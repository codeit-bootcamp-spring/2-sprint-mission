package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.Factory.CreateChannalFactory;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.file.FileServerRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ServerService;

import java.util.*;

public class FileServerService   {
//    private static volatile FileServerService instance;
//    private final Map<UUID, ServerRepository> serverTable = new HashMap<>();
//
//    private FileServerService() {
//    }
//
//    public static FileServerService getInstance() {
//        if (instance == null) {
//            synchronized (FileServerService.class) {
//                if (instance == null) {
//                    instance = new FileServerService();
//                }
//            }
//        }
//        return instance;
//    }
//
//    private ServerRepository getServerRepository(UUID id) {
//        ServerRepository serverRepository = serverTable.get(id);
//        if (serverRepository == null) {
//            ServerRepository repository = new FileServerRepository();
//            serverTable.put(id, repository);
//            serverRepository = repository;
//        }
//        return serverRepository;
//    }
//
//
//    @Override
//    public Channel createChannel(String name) {
//        return CreateChannalFactory.getInstance().create(name);
//    }
//
//    @Override
//    public void addChannel(UUID serverId, String name) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        Channel channel = CreateChannalFactory.getInstance().create(name);
//        serverRepository.save(channel);
//    }
//
//    @Override
//    public void addChannel(UUID serverId, Channel channel) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        serverRepository.save(channel);
//    }
//
//    @Override
//    public Channel getChannel(UUID serverId, String name) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        List<Channel> channelList = serverRepository.getContainerList();
//        Channel channel = channelList.stream().filter(c -> c.getName().equals(name))
//                .findFirst().orElse(null);
//        if (channel != null) {
//            //로그
//            System.out.println(channel.getName() + " 이(가) 반환됩니다.");
//            return channel;
//        }
//        //로그
//        System.out.println("존재하지 않습니다.");
//        return null;
//    }
//
//    @Override
//    public void printChannel(UUID serverId) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        List<Channel> channelList = serverRepository.getContainerList();
//        printChannel(channelList);
//    }
//
//
//    private void printChannel(List<Channel> list) {
//        System.out.println("\n=========채널 목록==========");
//        list.forEach(c-> System.out.println(c.getId() + " : " + c.getName()));
//        System.out.println("=========================\n");
//    }
//
//    @Override
//    public boolean removeChannel(UUID serverId) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        List<Channel> channelList = serverRepository.getContainerList();
//        if (channelList == null) {
//            System.out.println("채널 삭제 실패 : list null값");
//            return false;
//        }
//        Scanner sc = new Scanner(System.in);
//        System.out.print("삭제할 채널 이름을 입력하시오. : ");
//        String targetName = sc.nextLine();
//
//        return removeChannel(channelList, targetName, serverRepository);
//    }
//
//    @Override
//    public boolean removeChannel(UUID serverId, String targetName) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        List<Channel> channelList = serverRepository.getContainerList();
//        if (channelList == null) {
//            System.out.println("채널 삭제 실패 : list null값");
//            return false;
//        }
//
//        return removeChannel(channelList, targetName, serverRepository);
//    }
//
//
//    private boolean removeChannel(List<Channel> list, String targetName, ServerRepository serverRepository) {
//        Channel targetChannel = list.stream().filter(c -> c.getName().equals(targetName))
//                .findFirst().orElse(null);
//        if (targetChannel == null) {
//            System.out.println("삭제할 채널이 존재하지 않습니다.");
//            return false;
//        } else {
//            list.remove(targetChannel);
//            serverRepository.updateContainerList(list);
//        }
//        return true;
//    }
//
//    @Override
//    public boolean updateChannel(UUID serverId) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        List<Channel> channelList = serverRepository.getContainerList();
//
//        Scanner sc = new Scanner(System.in);
//        System.out.print("바꿀려고 하는 채널의 이름을 입력하시오. : ");
//        String targetName = sc.nextLine();
//
//        return updateChannel(serverId, channelList, targetName);
//    }
//
//    @Override
//    public boolean updateChannel(UUID serverId, String targetName) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        List<Channel> channelList = serverRepository.getContainerList();
//
//        Scanner sc = new Scanner(System.in);
//        System.out.print("채널 이름을 무엇으로 바꾸시겠습니까? : ");
//        String replaceName = sc.nextLine();
//
//        return updateChannel(serverId, channelList, targetName, replaceName);
//    }
//
//    @Override
//    public boolean updateChannel(UUID serverId, String targetName, String replaceName) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        List<Channel> channelList = serverRepository.getContainerList();
//
//        return updateChannel(serverId, channelList, targetName, replaceName);
//    }
//
//
//    private boolean updateChannel(UUID serverId, List<Channel> list, String targetName) {
//        Scanner sc = new Scanner(System.in);
//        System.out.print("채널 이름을 무엇으로 바꾸시겠습니까? : ");
//        String replaceName = sc.nextLine();
//
//        return updateChannel(serverId, list, targetName, replaceName);
//
//    }
//
//
//    private boolean updateChannel(UUID serverId, List<Channel> list, String targetName, String replaceName) {
//        ServerRepository serverRepository = getServerRepository(serverId);
//        Channel targetChannel = list.stream().filter(c -> c.getName().equals(targetName))
//                .findFirst().orElse(null);
//        if (targetChannel != null) {
//            targetChannel.setName(replaceName);
//            serverRepository.updateContainerList(list);
//            return true;
//        }
//        System.out.println("업데이트할 채널이 존재하지 않습니다.");
//        return false;
//    }
//

}
