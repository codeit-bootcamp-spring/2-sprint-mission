package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFUserRepository extends AbstractRepository<User> implements UserRepository {
    private static volatile JCFUserRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장

    private JCFUserRepository() {
        super(User.class, new ConcurrentHashMap<>());
    }

    public static JCFUserRepository getInstance() {
        // 첫 번째 null 체크 (성능 최적화)
        if (instance == null) {
            synchronized (JCFUserRepository.class) {
                // 두 번째 null 체크 (동기화 구간 안에서 중복 생성 방지)
                if (instance == null) {
                    instance = new JCFUserRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public void updateUserName(UUID userId, String newUserName) {
        if (existsById(userId)) {
            super.storage.get(userId).updateUserName(newUserName);
        }
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        if (existsById(userId)) {
            super.storage.get(userId).updateUserPassword(newPassword);
        }
    }
}
