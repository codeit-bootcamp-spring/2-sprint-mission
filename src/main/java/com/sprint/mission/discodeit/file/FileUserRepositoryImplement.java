package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository("fileUserRepositoryImplement")
public class FileUserRepositoryImplement implements UserRepository {
    private String dataDir;
    private String userDataFile;
    private final Map<UUID, User> userRepository;
    
    public FileUserRepositoryImplement() {
        this.dataDir = "./data";  // 기본 경로를 프로젝트 루트 아래 data 폴더로 설정
        this.userDataFile = "users.dat";
        userRepository = loadData();
        System.out.println("사용자 저장소 초기화 - 파일 경로: " + new File(dataDir, userDataFile).getAbsolutePath());
    }
    
    public FileUserRepositoryImplement(String dataDir) {
        this.dataDir = dataDir;
        this.userDataFile = "users.dat";
        userRepository = loadData();
    }
    
    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadData() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, userDataFile);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, User>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("파일 로드 오류: " + e.getMessage());
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
        
        File file = new File(dir, userDataFile);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(userRepository);
            System.out.println("사용자 데이터가 저장되었습니다: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("파일 저장 오류: " + e.getMessage());
            throw new RuntimeException("사용자 데이터 저장 실패", e);
        }
    }

    @Override
    public Optional<User> findByUser(UUID userId) {
        return Optional.ofNullable(userRepository.get(userId));
    }

    @Override
    public boolean register(User user) {
        userRepository.put(user.getId(), user);
        saveData();
        return true;
    }

    @Override
    public boolean deleteUser(UUID userId) {
        boolean removed = userRepository.remove(userId) != null;
        if (removed) {
            saveData();
        }
        return removed;
    }

    @Override
    public Set<UUID> findAllUsers() {
        return new HashSet<>(userRepository.keySet());
    }
    
    @Override
    public boolean updateUser(User user) {
        if (user == null || !userRepository.containsKey(user.getId())) {
            return false;
        }
        userRepository.put(user.getId(), user);
        saveData();
        return true;
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.values().stream()
                .filter(user -> user.getEmail() != null && user.getEmail().equals(email))
                .findFirst();
    }

    // 애플리케이션 종료 시 데이터 저장 보장
    @PreDestroy
    public void saveDataOnShutdown() {
        System.out.println("애플리케이션 종료 - 사용자 데이터 저장 중");
        saveData();
    }
}