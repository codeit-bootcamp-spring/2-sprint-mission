package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class FileMessageRepository implements MessageRepository {
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "messages");
    private final FileSerializationUtil fileUtil;

    public FileMessageRepository(FileSerializationUtil fileUtil){
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(Message message) {
        fileUtil.<Message>writeObjectToFile(message, FilePathUtil.getFilePath(directory, message.getId()));
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return fileUtil.<Message>readObjectFromFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public Optional<List<Message>> findAll() {
        try {
            List<Message> message = Files.list(directory)
                    .map(fileUtil::<Message>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            return Optional.of(message);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Message>> findByChannelId(UUID channelId) {
        try {
            List<Message> message = Files.list(directory)
                    .map(fileUtil::<Message>readObjectFromFile)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(item -> item.getChannelId().equals(channelId))
                    .toList();
            return Optional.of(message);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Message message) {
        save(message);
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }

    @Override
    public void deleteChannelById(UUID channelId) {
        findAll().ifPresent(messages -> {
            messages.stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .forEach(message -> {
                    fileUtil.deleteFile(FilePathUtil.getFilePath(directory, message.getId()));
                });
        });
    }
}
