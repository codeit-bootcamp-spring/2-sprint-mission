package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
@RequiredArgsConstructor
public class FileMessageRepository implements MessageRepository {

    private final FileManager fileManager;

    @Override
    public Message save(Message message) {
        fileManager.writeToFile(SubDirectory.MESSAGE, message, message.getId());
        return message;
    }

    @Override
    public Optional<Message> findMessageById(UUID messageUUID) {
        Optional<Message> message = fileManager.readFromFileById(SubDirectory.MESSAGE, messageUUID, Message.class);
        return message;
    }

    @Override
    public List<Message> findAllMessage() {
        List<Message> messageList = fileManager.readFromFileAll(SubDirectory.MESSAGE, Message.class);
        return messageList;
    }

    @Override
    public List<Message> findMessageByChannel(UUID channelUUID) {
        return findAllMessage().stream()
                .filter(message -> message.getChannelUUID().equals(channelUUID))
                .toList();
    }

    @Override
    public void delete(UUID messageUUID) {
        fileManager.deleteFileById(SubDirectory.MESSAGE, messageUUID);
    }
}
