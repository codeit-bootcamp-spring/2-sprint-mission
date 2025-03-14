package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.constant.SubDirectory;
import com.sprint.mission.discodeit.custom.AppendObjectOutputStream;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.utils.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

//@Repository
@RequiredArgsConstructor
public class FileMessageRepository implements MessageRepository {

    private final FileManager fileManager;

    @Override
    public Message save(UUID channelUUID, UUID userUUID, String content) {
        Message message = new Message(content, userUUID, channelUUID);
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
        return fileManager.readFromMessageOfChannel(channelUUID);
    }

    @Override
    public Message updateMessage(UUID messageUUID, String content) {
        Message message = findMessageById(messageUUID)
                .orElseThrow(() -> new IllegalArgumentException("메세지를 찾을 수 없습니다.: " + messageUUID));
        message.updateContent(content);
        fileManager.writeToFile(SubDirectory.MESSAGE, message, message.getId());
        return message;
    }

    @Override
    public boolean deleteMessageById(UUID messageUUID) {
        return fileManager.deleteFileById(SubDirectory.MESSAGE, messageUUID);
    }
}
