package com.sprint.mission.discodeit.Repository.file;

import com.sprint.mission.discodeit.DTO.Message.MessageCRUDDTO;
import com.sprint.mission.discodeit.Exception.NotFoundExceptions;
import com.sprint.mission.discodeit.Exception.EmptyExceptions;
import com.sprint.mission.discodeit.Repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.Repository.MessageRepository;
import com.sprint.mission.discodeit.Util.CommonUtils;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileMessageRepository implements MessageRepository {
    private final FileRepositoryImpl<Map<UUID, List<Message>>> fileRepository;
    private Map<UUID, List<Message>> messageList = new ConcurrentHashMap<>();

    private final Path path = Paths.get(System.getProperty("user.dir"), "data", "MessageList.ser");

    public FileMessageRepository() {
        this.fileRepository = new FileRepositoryImpl<>(path);
        fileRepository.load();
    }

    @Override
    public void reset() {
        fileRepository.init();
        try {
            Files.deleteIfExists(path);
            messageList = new ConcurrentHashMap<>();
        } catch (IOException e) {
            System.out.println("리스트 초기화 실패");
        }
    }

    @Override
    public void save(Channel channel, Message message) {
        List<Message> messages = messageList.getOrDefault(channel.getChannelId(), new ArrayList<>());
        messages.add(message);
        messageList.put(channel.getChannelId(), messages);
        fileRepository.save(messageList);
    }

    @Override
    public Message find(UUID messageId) {
        List<Message> list = messageList.values().stream().flatMap(List::stream).toList();
        Message message = CommonUtils.findById(list, messageId, Message::getMessageId)
                .orElseThrow(() -> NotFoundExceptions.MESSAGE_NOT_FOUND);

        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (messageList.isEmpty()) {
            throw EmptyExceptions.EMPTY_MESSAGE_LIST;
        }
        List<Message> messages = messageList.get(channelId);

        if (messages.isEmpty()) {
            throw EmptyExceptions.EMPTY_MESSAGE_LIST;
        }
        return messages;
    }

    @Override
    public UUID update(Message message, MessageCRUDDTO messageUpdateDTO) {
        if (messageUpdateDTO.text() != null) {
            message.setText(messageUpdateDTO.text());
        }
        if (messageUpdateDTO.messageId() != null) {
            message.setMessageId(messageUpdateDTO.messageId());
        }
        fileRepository.save(messageList);
        return message.getMessageId();
    }

    @Override
    public void remove(Channel channel, Message message) {
        List<Message> messages = messageList.get(channel.getChannelId());
        messages.remove(message);
        fileRepository.save(messageList);
    }
}
