package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.Exception.EmptyMessageListException;
import com.sprint.mission.discodeit.Exception.MessageNotFoundException;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private Map<UUID, List<Message>> messageList = new ConcurrentHashMap<>();
    private final Path path = Paths.get(System.getProperty("user.dir"), "data", "MessageList.ser");

    public FileChannelRepository() {
        loadMessageList();
    }

    private void init() {
        Path directory = path.getParent();
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료: " + directory);
            } catch (IOException e) {
                System.out.println("디렉토리 생성 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void loadMessageList() {
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                Map<UUID, List<Message>> list = (Map<UUID, List<Message>>) ois.readObject();
                messageList = list;

                System.out.println("메시지 리스트 로드 완료: " + path);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("메시지 리스트 로드 실패");
                throw new RuntimeException(e);
            }
        }
    }

    private void saveMessageList() {
        init();

        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(messageList);

        } catch (IOException e) {
            System.out.println("메시지 리스트 저장 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveMessage(Message message) {
        messageList.computeIfAbsent(message.getChannelId(), k -> new ArrayList<>()).add(message);

        saveMessageList();
    }

    @Override
    public List<Message> findMessageListByChannel(Channel channel) {
        return findMessageListByChannel(channel.getChannelId());
    }

    @Override
    public List<Message> findMessageListByChannel(UUID channelId) {
        List<Message> messages = Optional.ofNullable(messageList.get(channelId))
                .orElseThrow(() -> new EmptyMessageListException("메시지함이 비어있습니다."));
        return messages;
    }

    @Override
    public Message findMessageByChannel(Channel channel, UUID messageId) {
        List<Message> messages = findMessageListByChannel(channel);
        Message findMessage = messages.stream().filter(m -> m.getMessageId().equals(messageId))
                .findFirst().orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));
        return findMessage;
    }

    @Override
    public UUID updateMessage(Channel channel, Message message, String replaceText) {
        Message findMessage = findMessageByChannel(channel, message.getMessageId());
        findMessage.setText(replaceText);

        saveMessageList();
        return findMessage.getMessageId();
    }

    @Override
    public UUID removeMessage(Channel channel,  Message message) {
        List<Message> messages = findMessageListByChannel(channel);
        Message findMessage = findMessageByChannel(channel, message.getMessageId());

        messages.remove(findMessage);
        messageList.put(channel.getChannelId(), messages);

        saveMessageList();
        return findMessage.getMessageId();
    }
}
