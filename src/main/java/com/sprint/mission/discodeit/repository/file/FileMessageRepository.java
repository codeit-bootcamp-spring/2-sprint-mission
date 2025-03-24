package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Primary
@Repository
public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "messages.ser";

    @Override
    public void save(Message message) {
        if (message == null || message.getId() == null) {
            throw new IllegalArgumentException("메세지나 ID가 공백입니다.");
        }
        List<Message> messages = readFromFile();
        messages.removeIf(m -> m.getId() != null && m.getId().equals(message.getId()));
        messages.add(message);
        writeToFile(messages);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        if (id == null) return Optional.empty();
        return readFromFile().stream()
                .filter(m -> id.equals(m.getId()))
                .findFirst();
    }

    @Override
    public List<Message> findAll() {
        return readFromFile();
    }

    @Override
    public void delete(UUID id) {
        if (id == null) return;
        List<Message> messages = readFromFile();
        boolean removed = messages.removeIf(m -> id.equals(m.getId()));
        if (removed) {
            writeToFile(messages);
        }
    }

    private void writeToFile(List<Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Message> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            List<Message> messages = (List<Message>) ois.readObject();
            messages.removeIf(m -> m.getId() == null); // 데이터 정합성 체크
            return messages;
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
