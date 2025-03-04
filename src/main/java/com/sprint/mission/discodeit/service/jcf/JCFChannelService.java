package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.Repository.impl.LinkedListJCFChannelRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;


public class JCFChannelService implements ChannelService {
    private static JCFChannelService instance;
    private final Map<UUID, JCFChannelRepository> channelTable = new HashMap<>();

    private JCFChannelService(){
    }

    public static JCFChannelService getInstance() {
        if (instance == null) {
            instance = new JCFChannelService();
        }
        return instance;
    }

    private JCFChannelRepository getChannelRepository(UUID channelId) {
        JCFChannelRepository JCFChannelRepository = channelTable.get(channelId);
        if (JCFChannelRepository == null) {
            LinkedListJCFChannelRepository repository = new LinkedListJCFChannelRepository();
            channelTable.put(channelId, repository);
            JCFChannelRepository = repository;
        }
        return JCFChannelRepository;
    }

    @Override
    public Message write(UUID channelId) {
        JCFChannelRepository JCFChannelRepository = getChannelRepository(channelId);
        Scanner sc = new Scanner(System.in);
        System.out.print("메시지를 작성하시오. : ");
        String str = sc.nextLine();
        Message message = new Message(str);
        return write(JCFChannelRepository, message);
    }

    @Override
    public Message write(UUID channelId, String str) {
        JCFChannelRepository JCFChannelRepository = getChannelRepository(channelId);
        Message message = new Message(str);

        return write(JCFChannelRepository, message);
    }

    private Message write(JCFChannelRepository JCFChannelRepository, Message message) {
        JCFChannelRepository.add(message);
        return message;
    }

    @Override
    public Message getMessage(UUID channelId, String str) {
        JCFChannelRepository JCFChannelRepository = getChannelRepository(channelId);
        List<Message> list = JCFChannelRepository.getList();
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
        JCFChannelRepository JCFChannelRepository = getChannelRepository(channelId);
        List<Message> list = JCFChannelRepository.getList();
        printChannel(list);
    }

    @Override
    public void printChannel(List<Message> list) {
        System.out.println("\n=========메시지 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getName());
        }
        System.out.println("=========================\n");
    }

    @Override
    public boolean removeMessage(UUID channelId, String targetName) {
        JCFChannelRepository JCFChannelRepository = getChannelRepository(channelId);
        List<Message> list = JCFChannelRepository.getList();
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
        JCFChannelRepository JCFChannelRepository = getChannelRepository(id);
        List<Message> list = JCFChannelRepository.getList();
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
