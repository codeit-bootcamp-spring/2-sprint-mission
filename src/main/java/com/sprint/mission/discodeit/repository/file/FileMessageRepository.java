package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.config.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private final Path messagePath;

    public FileMessageRepository(Path messagePath) {
        this.messagePath = messagePath;
    }

    @Override
    public Message save(Message message) {
        Map<UUID, Message> messages = loadObjectsFromFile(messagePath);

        messages.put(message.getId(), message);
        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), messagePath, messages);

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

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), messagePath, messages);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> messages = loadObjectsFromFile(messagePath);
        messages.remove(id);
        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), messagePath, messages);
    }
}
