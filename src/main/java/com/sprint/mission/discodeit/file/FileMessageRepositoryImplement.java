package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageRepository;

import java.io.*;
import java.util.*;

/**
 * Message 저장소의 파일 기반 구현 클래스
 */
public class FileMessageRepositoryImplement implements MessageRepository {
    private static final String DATA_DIR = "data";
    private static final String MESSAGE_DATA_FILE = "messages.dat";
    
    private Map<UUID, Message> messageRepository;

    public FileMessageRepositoryImplement() {
        loadData();
    }
    
    /**
     * 메모리에 데이터를 파일로부터 로드합니다.
     */
    @SuppressWarnings("unchecked")
    private void loadData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, MESSAGE_DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                messageRepository = (Map<UUID, Message>) in.readObject();
                System.out.println("메시지 데이터 로드 완료: " + messageRepository.size() + "개의 메시지");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("메시지 데이터 로드 중 오류 발생: " + e.getMessage());
                messageRepository = new HashMap<>();
            }
        } else {
            messageRepository = new HashMap<>();
            System.out.println("새로운 메시지 저장소 생성");
        }
    }
    
    /**
     * 메모리의 데이터를 파일에 저장합니다.
     */
    private void saveData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("데이터 디렉토리 생성 실패: " + DATA_DIR);
                return;
            }
        }
        
        File file = new File(dir, MESSAGE_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(messageRepository);
            System.out.println("메시지 데이터 저장 완료: " + messageRepository.size() + "개의 메시지");
        } catch (IOException e) {
            System.err.println("메시지 데이터 저장 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void register(Message message) {
        messageRepository.put(message.getId(), message);
        saveData(); // 데이터 변경 시 저장
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageRepository.get(messageId));
    }

    @Override
    public List<Message> findAll() {
        // 방어적 복사를 통해 원본 데이터 보호
        return new ArrayList<>(messageRepository.values());
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        boolean result = messageRepository.remove(messageId) != null;
        if (result) {
            saveData(); // 데이터 변경 시 저장
        }
        return result;
    }
} 