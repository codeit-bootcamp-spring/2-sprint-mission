package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    private static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data", "message");

    public FileMessageRepository() {
        FileUtil.init(DIRECTORY);
    }

    @Override
    public void createMessage(Message message) {
        FileUtil.saveToFile(DIRECTORY, message, message.getId());
    }

    @Override
    public Optional<Message> selectMessageById(UUID id) {
        return FileUtil.loadFromFile(DIRECTORY, id);
    }

    @Override
    public List<Message> selectMessagesByChannel(UUID channelId) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof Message)
                .map(object -> (Message) object)
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void updateMessage(UUID id, String content, UUID userId, UUID channelId) {
        FileUtil.loadFromFile(DIRECTORY, id).ifPresent(object -> {
            if (object instanceof Message message) {
                message.updateContent(content);
                FileUtil.saveToFile(DIRECTORY, message, id);
            } else {
                throw new IllegalArgumentException("Message 타입의 객체가 아닙니다. " + id);
            }
        });
    }

    @Override
    public void deleteMessage(UUID id, UUID userId, UUID channelId) {
        FileUtil.deleteFile(DIRECTORY, id);
    }

}
