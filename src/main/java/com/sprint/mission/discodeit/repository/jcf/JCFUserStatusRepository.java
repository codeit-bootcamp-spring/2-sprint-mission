package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf", matchIfMissing = true)

public class JCFUserStatusRepository implements UserStatusRepository {

    // 인메모리 저장소: UUID(유저 아이디)를 키로 사용
    private final Map<UUID, UserStatus> store = new ConcurrentHashMap<>();

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        // store에 저장된 UserStatus 중, userId가 일치하는 첫번째를 Optional로 반환
        return store.values().stream()
                .filter(us -> us.getId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(UserStatus userStatus) {
        // 만약 userStatus의 id가 null이면 생성 (대개 UserStatus 생성 시 id가 부여된다면 필요 없을 수 있음)
        if (userStatus.getId() == null) {
            userStatus.setId(UUID.randomUUID());
        }
        store.put(userStatus.getId(), userStatus);
    }

    @Override
    public void delete(UserStatus userStatus) {
        store.remove(userStatus.getId());
    }
}
