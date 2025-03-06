package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileChannelService implements ChannelService {
    private static volatile FileChannelService instance;
    private final Map<UUID, ChannelRepository> channelTable = new HashMap<>();

    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "MessageList.ser");

    private FileChannelService() {
    }

    public static FileChannelService getInstance() {
        if (instance == null) {
            synchronized (FileChannelService.class) {
                if (instance == null) {
                    instance = new FileChannelService();
                }
            }
        }
        return instance;
    }

    private ChannelRepository getChannelRepository(UUID channelId) {
        ChannelRepository channelRepository = channelTable.get(channelId);
        if (channelRepository == null) {
            ChannelRepository repository = new FileChannelRepository();
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
        return message;
    }

    @Override
    public Message getMessage(UUID channelId, String str) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        List<Message> list = channelRepository.getList();
        for (Message message : list) {
            if (message.getStr().equals(str)) {
                //로그
                System.out.println(message.getStr() + " 이(가) 반환됩니다.");
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

    private void printChannel(List<Message> list) {
        System.out.println("\n=========채널 메시지 목록==========");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + 1 + " : " + list.get(i).getStr());
        }
        System.out.println("=========================\n");
    }

    @Override
    public boolean removeMessage(UUID channelId, String targetName) {
        ChannelRepository channelRepository = getChannelRepository(channelId);
        List<Message> list = channelRepository.getList();
        for (Message item : list) {
            if (item.getStr().equals(targetName)) {
                //로그
                System.out.println(item.getStr() + " 이(가) 삭제됩니다.");
                list.remove(item);
                channelRepository.updateMessageList(list);
                return true;
            }
        }
        System.out.println("존재하지 않습니다.");
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
        } else {
            for (Message message : list) {
                if (message.getId().equals(targetMessage.getId())) {
                    message.setStr(replaceName);
                    channelRepository.updateMessageList(list);
                }
            }
        }
        return true;
    }
}
