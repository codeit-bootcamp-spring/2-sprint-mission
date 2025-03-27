package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.time.Instant;
import java.util.*;

import static com.sprint.mission.discodeit.util.FileUtils.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {
    private final Path messagePath;

    public FileMessageRepository(@Value("${discodeit.repository.file-directory.message-path}") Path messagePath) {
        this.messagePath = messagePath;
    }

    @Override
    public Message save(Message message) {
        loadAndSaveConsumer(messagePath, (Map<UUID, Message> messages) ->
                messages.put(message.getId(), message)
        );

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
    public Message updateContext(UUID id, String context) {
        return loadAndSave(messagePath, (Map<UUID, Message> messages) -> {
                    Message message = messages.get(id);
                    message.updateContext(context);
                    return message;
                }
        );
    }

    @Override
    public void delete(UUID id) {
        loadAndSaveConsumer(messagePath, (Map<UUID, Message> messages) ->
                messages.remove(id)
        );
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        loadAndSaveConsumer(messagePath, (Map<UUID, Message> messages) ->
                messages.values().removeIf(message -> message.getChannelId().equals(channelId))
        );
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
