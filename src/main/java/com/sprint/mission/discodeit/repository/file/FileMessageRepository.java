package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "messages.ser";

    @Override
    public void save(Message message) {
        List<Message> messages = findAll();
        messages.removeIf(msg -> msg.getId().equals(message.getId())); // 중복 제거
        messages.add(message);
        writeToFile(messages);
    }

    @Override
    public Message findById(UUID id) {
        return findAll().stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Message> findAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeToFile(List<Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID id) {
        List<Message> messages = findAll();
        boolean removed = messages.removeIf(message -> message.getId().equals(id));

        if (removed) {
            writeToFile(messages);
        }
    }
}
