package com.sprint.mission.discodeit.basic.repositoryimpl;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;

import java.util.*;

public class BasicUserRepositoryImplement implements UserRepository {
    private final Map<UUID, User> userRepository; //유저아이디/유저
    
    // 싱글톤 인스턴스
    private static BasicUserRepositoryImplement instance;
    
    // private 생성자로 변경
    private BasicUserRepositoryImplement() {
        userRepository = new HashMap<>();
    }
    
    // 싱글톤 인스턴스를 반환하는 정적 메소드
    public static synchronized BasicUserRepositoryImplement getInstance() {
        if (instance == null) {
            instance = new BasicUserRepositoryImplement();
        }
        return instance;
    }

    @Override
    public Optional<User> findByUser(UUID userId) {
        return Optional.ofNullable(userRepository.get(userId));
    }

    @Override
    public boolean register(User user) {
        userRepository.put(user.getId(), user);
        return true;
    }

    @Override
    public boolean deleteUser(UUID userId) {//유저제거
        return userRepository.remove(userId) != null;
    }

    @Override
    //map  없을 경우 빈 set() 반환
    public Set<UUID> findAllUsers() {
        return new HashSet<>(userRepository.keySet());
    }
    //업데이트 시 저장
    @Override
    public boolean updateUser(User user) {
        return userRepository.put(user.getId(), user)!=null;

    }
}



