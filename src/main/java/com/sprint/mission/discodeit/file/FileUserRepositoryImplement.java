package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository("fileUserRepository")
public class FileUserRepositoryImplement implements UserRepository {
    private static final String DATA_DIR = "data";
    private static final String USER_DATA_FILE = "users.dat";
    
    private final Map<UUID, User> userRepository;
    
    private String FILEPATH = "./data/user_data.ser";
    
    public FileUserRepositoryImplement() {
        userRepository = loadData();
    }
    
    public FileUserRepositoryImplement(String dataDir) {
        FILEPATH = dataDir + "/user_data.ser";
        userRepository = loadData();
    }
    
    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, USER_DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (Map<UUID, User>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("사용자 데이터 로드 중 오류 발생: " + e.getMessage());
            }
        }
        
        return new HashMap<>();
    }
    
    private synchronized void saveData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File file = new File(dir, USER_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(userRepository);
        } catch (IOException e) {
            System.err.println("사용자 데이터 저장 중 오류 발생: " + e.getMessage());
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

    }