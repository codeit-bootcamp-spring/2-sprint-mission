package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.time.Instant;
import java.util.*;

import static com.sprint.mission.discodeit.constant.FilePath.MESSAGE_TEST_FILE;
import static com.sprint.mission.discodeit.constant.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

@Repository
public class FileMessageRepository implements MessageRepository {
    private Path messagePath = MESSAGE_TEST_FILE;

    public void changePath(Path path) {
        this.messagePath = path;
    }

    @Override
    public Message save(Message message) {
        Map<UUID, Message> messages = loadObjectsFromFile(messagePath);

        messages.put(message.getId(), message);
        saveObjectsToFile(STORAGE_DIRECTORY, messagePath, messages);

        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        Map<UUID, Message> messages = loadObjectsFromFile(messagePath);

        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public List<Message> findAll() {
        Map<UUID, Message> messages = loadObjectsFromFile(messagePath);

        return messages.values()
                .stream()
                .toList();
    }

    @Override
    public void updateContext(UUID id, String context) {
        Map<UUID, Message> messages = loadObjectsFromFile(messagePath);
        messages.get(id)
                .updateContext(context);

        saveObjectsToFile(STORAGE_DIRECTORY, messagePath, messages);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> messages = loadObjectsFromFile(messagePath);
        messages.remove(id);
        saveObjectsToFile(STORAGE_DIRECTORY, messagePath, messages);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, Message> messages = loadObjectsFromFile(messagePath);
        List<UUID> sameChannelMessageIds = messages.values()
                .stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(Message::getId)
                .toList();

        for (UUID messageId : sameChannelMessageIds) {
            messages.remove(messageId);
        }
    }


    @Override
    public Instant findLastMessageCreatedAtByChannelId(UUID channelId) {
        Map<UUID, Message> messages = loadObjectsFromFile(messagePath);

        return messages.values()
                .stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(Instant.ofEpochSecond(0));
    }
}
