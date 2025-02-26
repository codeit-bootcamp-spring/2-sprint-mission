package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.Repository.impl.LinkedListChannelRepository;
import com.sprint.mission.discodeit.entity.Container.Container;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;


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

    private ChannelRepository getChannelRepository(UUID channelId) {
        ChannelRepository channelRepository = channelTable.get(channelId);
        if (channelRepository == null) {
            LinkedListChannelRepository repository = new LinkedListChannelRepository();
            channelTable.put(channelId, repository);
            channelRepository = repository;
        }
        return channelRepository;
    }

    @Override
    public Message write(UUID channelId) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        Scanner sc = new Scanner(System.in);
        System.out.printf("메시지를 작성하시오. : ");
        String str = sc.nextLine();
        Message message = new Message(str);
        return write(channelRepository, message);
    }

    @Override
    public Message write(UUID channelId, String str) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        Message message = new Message(str);

        return write(channelRepository, message);
    }

    private Message write(ChannelRepository channelRepository, Message message) {
        channelRepository.add(message);
        return message;
    }

    @Override
    public Message getMessage(UUID channelId, String str) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        List<Message> list = channelRepository.getList();
        for (Message message : list) {
            if (message.getName().equals(str)) {
                //로그
                System.out.println(message.getName() + " 이(가) 반환됩니다.");
                return message;
            }
        }
        //로그
        System.out.println("존재하지 않습니다.");
        return null;
    }

    @Override
    public void printChannel(UUID channelId) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
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
    public boolean removeMessage(UUID channelId, String targetName) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
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
                System.out.println(targetName+" 이(가) " + item.getName() + " 이(가) 됩니다.");
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
        return false;
    }
}
