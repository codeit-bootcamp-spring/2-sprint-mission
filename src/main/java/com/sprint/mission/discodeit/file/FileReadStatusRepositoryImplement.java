package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository("fileReadStatusRepository")
public class FileReadStatusRepositoryImplement implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusRepository;
    private String FILEPATH = "./data/read_status_data.ser";
    
    public FileReadStatusRepositoryImplement() {
        readStatusRepository = loadData();
    }
    
    public FileReadStatusRepositoryImplement(String dataDir) {
        FILEPATH = dataDir + "/read_status_data.ser";
        readStatusRepository = loadData();
    }
    
    @SuppressWarnings("unchecked")
    private Map<UUID, ReadStatus> loadData() {
        try {
            File file = new File(FILEPATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                return new HashMap<>();
            }
            
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, ReadStatus>) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("읽음 상태 데이터 로드 실패: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    private void saveData() {
        try {
            File file = new File(FILEPATH);
            file.getParentFile().mkdirs();
            
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(readStatusRepository);
            }
        } catch (IOException e) {
            System.err.println("읽음 상태 데이터 저장 실패: " + e.getMessage());
        }
    }
    
    @Override
    public boolean register(ReadStatus readStatus) {
        readStatusRepository.put(readStatus.getId(), readStatus);
        saveData();
        return true;
    }
    
    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusRepository.get(id));
    }
    
    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatusRepository.values());
    }
    
    @Override
    public boolean updateReadStatus(ReadStatus readStatus) {
        if (readStatus == null || !readStatusRepository.containsKey(readStatus.getId())) {
            return false;
        }
        
        readStatusRepository.put(readStatus.getId(), readStatus);
        saveData();
        return true;
    }
    
    @Override
    public boolean deleteReadStatus(UUID id) {
        boolean removed = readStatusRepository.remove(id) != null;
        if (removed) {
            saveData();
        }
        return removed;
    }
    
    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusRepository.values().stream()
                .filter(status -> status.getUserId().equals(userId) && status.getChannelId().equals(channelId))
                .findFirst();
    }
    
    public boolean deleteAllByUserId(UUID userId) {
        boolean success = true;
        List<ReadStatus> statusesToDelete = new ArrayList<>();
        
        // 먼저 삭제할 항목 수집
        for (ReadStatus status : readStatusRepository.values()) {
            if (userId.equals(status.getUserId())) {
                statusesToDelete.add(status);
            }
        }
        
        // 삭제 실행
        for (ReadStatus status : statusesToDelete) {
            if (!deleteReadStatus(status.getId())) {
                success = false;
            }
        }
        
        return success;
    }
    
    @Override
    public boolean deleteAllByChannelId(UUID channelId) {
        boolean success = true;
        List<ReadStatus> statusesToDelete = new ArrayList<>();
        
        // 먼저 삭제할 항목 수집
        for (ReadStatus status : readStatusRepository.values()) {
            if (channelId.equals(status.getChannelId())) {
                statusesToDelete.add(status);
            }
        }
        
        // 삭제 실행
        for (ReadStatus status : statusesToDelete) {
            if (!deleteReadStatus(status.getId())) {
                success = false;
            }
        }
        
        return success;
    }
} 