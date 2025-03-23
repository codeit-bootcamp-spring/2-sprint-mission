package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository("fileMessageRepository")
public class FileMessageRepositoryImplement implements MessageRepository {
    private String DATA_DIR = "data";
    private String MESSAGE_DATA_FILE = "messages.dat";
    
    private final Map<UUID, Message> messageRepository;
    
    public FileMessageRepositoryImplement() {
        messageRepository = loadData();
    }
    
    public FileMessageRepositoryImplement(String dataDir) {
        DATA_DIR = dataDir;
        MESSAGE_DATA_FILE = "messages.dat";
        messageRepository = loadData();
    }
    
    @SuppressWarnings("unchecked")
    private Map<UUID, Message> loadData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, MESSAGE_DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, Message>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("메시지 데이터 로드 중 오류 발생: " + e.getMessage());
            }
        }
        
        return new HashMap<>();
    }
    
    private synchronized void saveData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, MESSAGE_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(messageRepository);
        } catch (IOException e) {
            System.err.println("메시지 데이터 저장 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public boolean register(Message message) {
        messageRepository.put(message.getId(), message);
        saveData();
        return true;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageRepository.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageRepository.values());
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        boolean removed = messageRepository.remove(messageId) != null;
        if (removed) {
            saveData();
        }
        return removed;
    }


    @Override
    public boolean updateMessage(Message message) {
        if (message == null || !messageRepository.containsKey(message.getId())) {
            return false;
        }
        messageRepository.put(message.getId(), message);
        saveData();
        return true;
    }

    public List<Message> findAllByAuthorId(UUID authorId) {
        return messageRepository.values().stream()
            .filter(message -> authorId.equals(message.getAuthorId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.values().stream()
            .filter(message -> message.getChannelId() != null && message.getChannelId().equals(channelId))
            .collect(Collectors.toList());
    }
} 