package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.legacy.message.MessageCRUDDTO;
import com.sprint.mission.discodeit.exception.Empty.EmptyMessageListException;
import com.sprint.mission.discodeit.exception.NotFound.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.NotFound.SaveFileNotFoundException;
import com.sprint.mission.discodeit.repository.FileRepositoryImpl;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.CommonUtils;
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
        try {
            this.messageList = fileRepository.load();
        } catch (SaveFileNotFoundException e) {
            System.out.println("FileMessageRepository init");
        }
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
    public Message save(Channel channel, Message message) {
        List<Message> messages = messageList.getOrDefault(channel.getChannelId(), new ArrayList<>());
        messages.add(message);
        messageList.put(channel.getChannelId(), messages);
        fileRepository.save(messageList);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        List<Message> list = messageList.values().stream().flatMap(List::stream).toList();
        Message message = CommonUtils.findById(list, messageId, Message::getMessageId)
                .orElseThrow(() -> new MessageNotFoundException("메시지를 찾을 수 없습니다."));

        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (messageList.isEmpty()) {
            throw new EmptyMessageListException("Repository 에 저장된 메시지 리스트가 없습니다.");
        }
        List<Message> messages = messageList.get(channelId);

        if (messages.isEmpty()) {
            throw new EmptyMessageListException("해당 채널에 저장된 메시지 리스트가 없습니다.");
        }
        return messages;
    }

    @Override
    public List<Message> findAllByMessageId(UUID messageId) {
        if (messageList.isEmpty()) {
            throw new EmptyMessageListException("Repository 에 저장된 메시지 리스트가 없습니다.");
        }
        List<Message> messages = messageList.values().stream().flatMap(List::stream)
                .filter(message -> message.getMessageId().equals(messageId))
                .toList();

        if (messages.isEmpty()) {
            throw new EmptyMessageListException("해당 채널에 저장된 메시지 리스트가 없습니다.");
        }
        return messages;
    }

    @Override
    public Message update(Message message, MessageCRUDDTO messageUpdateDTO) {
        if (messageUpdateDTO.text() != null) {
            message.setText(messageUpdateDTO.text());
        }
        if (messageUpdateDTO.messageId() != null) {
            message.setMessageId(messageUpdateDTO.messageId());
        }
        fileRepository.save(messageList);
        return message;
    }

    @Override
    public void remove(UUID messageId) {
        List<Message> messages = findAllByMessageId(messageId);
        Message message = find(messageId);
        messages.remove(message);
        fileRepository.save(messageList);
    }
}
