package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository("fileUserStatusRepository")
public class FileUserStatusRepositoryImpl implements UserStatusRepository {
    private String DATA_DIR = "data";
    private String USER_STATUS_DATA_FILE = "user_status.dat";
    
    private final Map<UUID, UserStatus> userStatuses;
    
    public FileUserStatusRepositoryImpl() {
        userStatuses = loadData();
    }
    
    public FileUserStatusRepositoryImpl(String dataDir) {
        DATA_DIR = dataDir;
        USER_STATUS_DATA_FILE = "user_status.dat";
        userStatuses = loadData();
    }
    
    @SuppressWarnings("unchecked")
    private Map<UUID, UserStatus> loadData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, USER_STATUS_DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, UserStatus>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("사용자 상태 데이터 로드 중 오류 발생: " + e.getMessage());
            }
        }
        
        return new HashMap<>();
    }
    
    private synchronized void saveData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, USER_STATUS_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(userStatuses);
        } catch (IOException e) {
            System.err.println("사용자 상태 데이터 저장 중 오류 발생: " + e.getMessage());
        }
    }
    
    @Override
    public boolean register(UserStatus userStatus) {
        userStatuses.put(userStatus.getId(), userStatus);
        saveData();
        return true;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatuses.get(id));
    }
    
    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatuses.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatuses.values());
    }
    
    @Override
    public boolean update(UserStatus userStatus) {
        if (userStatuses.containsKey(userStatus.getId())) {
            userStatuses.put(userStatus.getId(), userStatus);
            saveData();
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(UUID id) {
        boolean result = userStatuses.remove(id) != null;
        if (result) {
            saveData();
        }
        return result;
    }
} 