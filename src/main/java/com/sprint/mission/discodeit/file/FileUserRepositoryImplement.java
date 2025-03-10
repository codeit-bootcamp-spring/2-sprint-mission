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

    public FileUserRepositoryImplement() {
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
        
        File file = new File(dir, USER_DATA_FILE);
        
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                userRepository = (Map<UUID, User>) in.readObject();
                System.out.println("사용자 데이터 로드 완료: " + userRepository.size() + "명의 사용자");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("사용자 데이터 로드 중 오류 발생: " + e.getMessage());
                userRepository = new HashMap<>();
            }
        } else {
            userRepository = new HashMap<>();
            System.out.println("새로운 사용자 저장소 생성");
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
        
        File file = new File(dir, USER_DATA_FILE);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(userRepository);
            System.out.println("사용자 데이터 저장 완료: " + userRepository.size() + "명의 사용자");
        } catch (IOException e) {
            System.err.println("사용자 데이터 저장 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByUser(UUID userId) {
        return Optional.ofNullable(userRepository.get(userId));
    }

    @Override
    public boolean registerUserId(User user) {
        userRepository.put(user.getId(), user);
        saveData(); // 데이터 변경 시 저장
        return true;
    }

    @Override
    public boolean removeUser(UUID userId) {
        boolean result = userRepository.remove(userId) != null;
        if (result) {
            saveData(); // 데이터 변경 시 저장
        }
        return result;
    }

    @Override
    public Set<UUID> findAllUsers() {
        // 방어적 복사를 통해 원본 데이터 보호
        return new HashSet<>(userRepository.keySet());
    }
} 