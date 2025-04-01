package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    //
    private Map<UUID, Message> messageData;
    private final Path messageFilePath;

    public FileMessageRepository(@Value("${discodeit.repository.file-directory:data}") String fileDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory, Message.class.getSimpleName());
        this.messageFilePath = DIRECTORY.resolve("message" + EXTENSION);

        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage(), e);
            }
        }
        dataLoad();
    }

    private void dataLoad() {
        if (!Files.exists(messageFilePath)) {
            messageData = new HashMap<>();
            dataSave();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(messageFilePath.toFile()))) {
            messageData = (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일을 불러올 수 없습니다.", e);
        }
    }

    private void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(messageFilePath.toFile()))) {
            oos.writeObject(messageData);
        } catch (IOException e) {
            throw new RuntimeException("파일을 저장할 수 없습니다."+ e.getMessage(), e);
        }
    }

    @Override
    public Message save(Message message){
        this.messageData.put(message.getId(), message);

        dataSave();
        return message;
    }

    @Override
    public Map<UUID, Message> getMessageData(){
        return messageData;
    }

    @Override
    public List<Message> findAll(){
        return this.messageData.values().stream().toList();
    }

    @Override
    public Message findById(UUID messageId){
        return Optional.ofNullable(messageData.get(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    public Message update(Message message, String newContent){
        message.update(newContent);

        dataSave();
        return message;
    }

    @Override
    public void delete(UUID messageId){
        messageData.remove(messageId);
        dataSave();
    }
}
