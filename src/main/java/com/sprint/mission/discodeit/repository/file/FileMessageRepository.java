package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileMessageRepository implements MessageRepository {
    private final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map",
            Message.class.getSimpleName());

    public FileMessageRepository() {
        FileUtil.init(DIRECTORY);
    }

    @Override
    public Message save(Message message) {
        return FileUtil.save(DIRECTORY, message, message.getId());
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return FileUtil.findById(DIRECTORY, id, Message.class);
    }

    @Override
    public List<Message> findAll() {
        return FileUtil.findAll(DIRECTORY, Message.class);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return FileUtil.existsById(DIRECTORY, id);
    }

    @Override
    public void deleteById(UUID id) {
        FileUtil.delete(DIRECTORY, id);
    }
}
