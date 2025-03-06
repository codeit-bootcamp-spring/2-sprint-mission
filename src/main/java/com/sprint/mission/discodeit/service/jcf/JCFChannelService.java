package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;


public class JCFChannelService implements ChannelService {
    private static volatile JCFChannelService instance;
    private final Map<UUID, ChannelRepository> channelTable = new HashMap<>();

    private JCFChannelService() {
    }

    public static JCFChannelService getInstance() {
        if (instance == null) {
            synchronized (JCFChannelService.class) {
                if (instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }
        return instance;
    }

    private ChannelRepository getChannelRepository(UUID channelId) {
        ChannelRepository channelRepository = channelTable.get(channelId);
        if (channelRepository == null) {
            ChannelRepository repository = new JCFChannelRepository();
            channelTable.put(channelId, repository);
            channelRepository = repository;
        }
        return channelRepository;
    }

    @Override
    public Message write(UUID channelId) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        Scanner sc = new Scanner(System.in);
        System.out.print("메시지를 작성하시오. : ");
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
        channelRepository.save(message);
        System.out.println("메시지 생성 : " + message.getStr());
        return message;
    }

    @Override
    public Message getMessage(UUID channelId, String str) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        List<Message> list = channelRepository.getList();
        Message message = list.stream().filter(m -> m.getStr().equals(str))
                .findFirst().orElse(null);
        if (message != null) {
            //로그
            System.out.println(message.getStr() + " 이(가) 반환됩니다.");
            return message;
        }
        //로그
        System.out.println("해당 메시지가 존재하지 않습니다.");
        return null;
    }

    @Override
    public void printChannel(UUID channelId) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        List<Message> list = channelRepository.getList();
        printChannel(list);
    }

    private void printChannel(List<Message> list) {
        System.out.println("\n=========채널 메시지 목록==========");
        list.forEach(m -> System.out.println(m.getStr()));
        System.out.println("=========================\n");
    }

    @Override
    public boolean removeMessage(UUID channelId, String targetName) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        List<Message> list = channelRepository.getList();
        Message message = list.stream().filter(m -> m.getStr().equals(targetName))
                .findFirst().orElse(null);
        if (message != null) {
            //로그
            System.out.println(message.getStr() + " 이(가) 삭제됩니다.");
            list.remove(message);
            return true;
        }
        System.out.println("해당 메시지가 존재하지 않습니다.");
        return false;
    }

    @Override
    public boolean updateMessage(UUID channelId, String targetName, String replaceName) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        List<Message> list = channelRepository.getList();
        Message targetMessage = list.stream().filter(m -> m.getStr().equals(targetName))
                .findFirst().orElse(null);
        if (targetMessage == null) {
            System.out.println("업데이트할 메시지가 존재하지 않습니다.");
            return false;
        }
        targetMessage.setStr(replaceName);
        //로그
        System.out.println(targetName + " 이(가) " + targetMessage.getStr() + " 이(가) 됩니다.");
        return true;
    }
}
