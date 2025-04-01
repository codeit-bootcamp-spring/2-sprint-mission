package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository("fileUserStatusRepositoryImpl")
public class FileUserStatusRepositoryImpl implements UserStatusRepository {
    private String dataDir;
    private String userStatusDataFile;
    
    private final Map<UUID, UserStatus> userStatuses;

    public FileUserStatusRepositoryImpl(String dataDir) {
        this.dataDir = dataDir;
        this.userStatusDataFile = "user_status.dat";
        userStatuses = loadData();
    }
    
    @SuppressWarnings("unchecked")
    private Map<UUID, UserStatus> loadData() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, userStatusDataFile);
        System.out.println("사용자 상태 데이터 로드 경로: " + file.getAbsolutePath());
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, UserStatus>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("사용자 상태 데이터 로드 오류: " + e.getMessage());
                return new HashMap<>();
            }
        }
        
        return new HashMap<>();
    }
    
    private synchronized void saveData() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, userStatusDataFile);
        System.out.println("사용자 상태 데이터 저장 경로: " + file.getAbsolutePath());
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(userStatuses);
        } catch (IOException e) {
            System.err.println("사용자 상태 데이터 저장 오류: " + e.getMessage());
            throw new RuntimeException("사용자 상태 데이터 저장 실패", e);
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

    // 애플리케이션 종료 시 데이터 저장 보장
    @PreDestroy
    public void saveDataOnShutdown() {
        System.out.println("애플리케이션 종료 - 사용자 상태 데이터 저장 중");
        saveData();
    }
} 