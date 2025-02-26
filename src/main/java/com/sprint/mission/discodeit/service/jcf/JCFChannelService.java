package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.impl.LinkedListChannelRepository;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class JCFChannelService implements ChannelService {
    private static JCFChannelService instance;
    private final Map<UUID, ChannelRepository> channelTable = new HashMap<>();

    private JCFChannelService(){
    }

    public static JCFChannelService getInstance() {
        if (instance == null) {
            instance = new JCFChannelService();
        }
        return instance;
    }

    private ChannelRepository getChannelRepository(UUID id) {
        ChannelRepository channelRepository = channelTable.get(id);
        if (channelRepository == null) {
            LinkedListChannelRepository repository = new LinkedListChannelRepository();
            channelTable.put(id, repository);
            channelRepository = repository;
        }
        return channelRepository;
    }

    @Override
    public Message write(UUID id, String name) {
        ChannelRepository channelRepository = getChannelRepository(id);
        Message message = new Message(name);
        channelRepository.add(message);
        return message;
    }

    @Override
    public void printChannel(UUID id) {
        ChannelRepository channelRepository = getChannelRepository(id);
        List<Message> list = channelRepository.getList();
        printChannel(list);
    }

    @Override
    public void printChannel(List<Message> list) {
        System.out.println("=========메시지 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
        System.out.println("=========================");
    }

    @Override
    public boolean removeMessage(UUID id, String targetName) {
        ChannelRepository channelRepository = getChannelRepository(id);
        List<Message> list = channelRepository.getList();
        for (Message item : list) {
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
    public boolean updateMessage(UUID id, String targetName, String replaceName) {
        ChannelRepository channelRepository = getChannelRepository(id);
        List<Message> list = channelRepository.getList();
        for (Message item : list) {
            if (item.getName().equals(targetName)) {
                item.setName(replaceName);
                //로그
                System.out.println(targetName+" 이 " + item.getName() + " 이(가) 됩니다.");
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }
}
