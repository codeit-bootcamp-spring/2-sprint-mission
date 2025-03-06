package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.config.FilePath.MESSAGE_FILE;
import static com.sprint.mission.config.FilePath.STORAGE_DIRECTORY;
import static com.sprint.mission.util.FileUtils.loadObjectsFromFile;
import static com.sprint.mission.util.FileUtils.saveObjectsToFile;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    @Override
    public Message save(Message message) {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());

        messages.put(message.getId(), message);
        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), MESSAGE_FILE.getPath(), messages);

        return message;
    }

    @Override
    public Message findById(UUID id) {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());

        return messages.get(id);
    }

    @Override
    public List<Message> findAll() {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());

        return messages.values()
                .stream()
                .toList();
    }

    @Override
    public void updateContext(UUID id, String context) {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());
        messages.get(id)
                .updateContext(context);

        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), MESSAGE_FILE.getPath(), messages);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> messages = loadObjectsFromFile(MESSAGE_FILE.getPath());
        messages.remove(id);
        saveObjectsToFile(STORAGE_DIRECTORY.getPath(), MESSAGE_FILE.getPath(), messages);
    }
}
