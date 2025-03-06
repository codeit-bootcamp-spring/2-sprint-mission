package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {

    private static final String FILE_PATH = "src/main/resources/messages.dat";
    private static Map<UUID, Message> messages = new HashMap<>();

    public FileMessageRepository() {
        loadFile();
    }

    private void loadFile(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            messages = (Map<UUID, Message>) ois.readObject();
        } catch (EOFException e) {
            System.out.println("⚠ messages.dat 파일이 비어 있습니다. 빈 데이터로 유지합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메세지 로드 중 오류 발생", e);
        }
    }

    private void saveFile(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException("메세지 저장 중 오류 발생", e);
        }
    }

    @Override
    public void save() {
        saveFile();
    }

    @Override
    public void addMessage(Message message) {
        messages.put(message.getId(), message);
        saveFile();
    }

    @Override
    public Message findMessageById(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public List<Message> findMessageAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void deleteMessageById(UUID messageId) {
        messages.remove(messageId);
        saveFile();
    }

    @Override
    public boolean existsById(UUID messageId) {
        return messages.containsKey(messageId);
    }
}
