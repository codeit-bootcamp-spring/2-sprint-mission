package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public FileMessageService(UserRepository userRepository, ChannelRepository channelRepository,
                              MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
        Path directory = Paths.get(System.getProperty("user.dir"),
                "src/main/java/com/sprint/mission/discodeit/data/Message");
        init(directory);
    }

    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getUpdatedMessages() {
        return messageRepository.findUpdatedMessages();
    }

    @Override
    public void registerMessage(UUID channelId, String userName, String messageContent) {
        messageRepository.createMessage(
                channelRepository.findById(channelId),
                userRepository.findByName(userName),
                messageContent
        );
    }

    @Override
    public void updateMessage(UUID messageId, String messageContent) {
        messageRepository.updateMessage(messageId, messageContent);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.deleteMessage(messageId);
    }

}
