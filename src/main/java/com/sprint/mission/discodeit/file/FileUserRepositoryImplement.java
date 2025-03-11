package com.sprint.mission.discodeit.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;

import java.io.*;
import java.util.*;

/**
 * User 저장소의 파일 기반 구현 클래스
 */
public class FileUserRepositoryImplement implements UserRepository {
    private static final String DATA_DIR = "data";
    private static final String USER_DATA_FILE = "users.dat";
    
    private Map<UUID, User> userRepository;
    
    // 싱글톤 인스턴스
    private static FileUserRepositoryImplement instance;
    
    // private 생성자로 변경
    private FileUserRepositoryImplement() {
        loadData();
    }
    
    // 싱글톤 인스턴스를 반환하는 정적 메소드
    public static synchronized FileUserRepositoryImplement getInstance() {
        if (instance == null) {
            instance = new FileUserRepositoryImplement();
        }
        return instance;
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
        
        File file = new File(dir, USER_DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                userRepository = (Map<UUID, User>) in.readObject();
                System.out.println("사용자 데이터 로드 완료: " + userRepository.size() + "명의 사용자");
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("사용자 데이터 로드 중 오류 발생: " + e.getMessage());
            }
        } else {
            userRepository = new HashMap<>();
            System.out.println("새로운 사용자 저장소 생성");
        }
    }
    
    /**
     * 메모리의 데이터를 파일에 저장합니다.
     */
    private synchronized void saveData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("데이터 디렉토리 생성 실패: " + DATA_DIR);
            }
        }
        
        File file = new File(dir, USER_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(userRepository);
            System.out.println("사용자 데이터 저장 완료: " + userRepository.size() + "명의 사용자");
        } catch (IOException e) {
            throw new RuntimeException("사용자 데이터 저장 중 오류 발생: " + e.getMessage());
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
        saveData();
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
} 