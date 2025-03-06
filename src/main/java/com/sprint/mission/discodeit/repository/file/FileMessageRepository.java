package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FilePathUtil;
import com.sprint.mission.discodeit.util.FileSerializationUtil;

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
    private static FileMessageRepository messageRepository;


    private FileMessageRepository(FileSerializationUtil fileUtil){
        this.fileUtil = fileUtil;
    }

    public static FileMessageRepository getInstance(FileSerializationUtil fileUtil){
        if(messageRepository == null){
            messageRepository = new FileMessageRepository(fileUtil);
        }

        return messageRepository;
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
    public void update(Message message) {
        save(message);
    }

    @Override
    public void delete(UUID id) {
        fileUtil.deleteFile(FilePathUtil.getFilePath(directory, id));
    }
}
