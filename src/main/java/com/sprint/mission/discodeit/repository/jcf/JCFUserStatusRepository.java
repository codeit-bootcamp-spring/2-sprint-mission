package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserStatusRepository implements UserStatusRepository {

    private final Map<UUID, UserStatus> data = new HashMap<>();

    @Override
    public UUID createUserStatus(UserStatus userStatus) {
        validateUserStatusDoesNotExist(userStatus.getUserId());

        data.put(userStatus.getId(), userStatus);
        return findById(userStatus.getId()).getId();
    }

    @Override
    public UserStatus findById(UUID id) {
        UserStatus userStatus = data.get(id);
        if(userStatus == null) {
            throw new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + id);
        }
        return userStatus;
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return data.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId));
    }

    public UserStatus findByUserIdOrNull(UUID userId) {
        return data.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUserStatus(UUID id, UUID userId, Instant lastActiveAt) {
        checkUserStatusExists(id);

        UserStatus userStatus = data.get(id);
        userStatus.update(lastActiveAt);
    }

    @Override
    public void updateByUserId(UUID userId, Instant now) {
        checkUserStatusExistsByUserId(userId);

        UserStatus userStatus = findByUserId(userId);
        userStatus.updateByUserId();
    }

    @Override
    public void deleteUserStatus(UUID id) {
        checkUserStatusExists(id);

        data.remove(id);
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkUserStatusExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 UserStatus를 찾을 수 없습니다: " + id);
        }
    }

    private void checkUserStatusExistsByUserId(UUID userId) {
        if(findByUserId(userId) == null){
            throw new NoSuchElementException("해당 사용자의 UserStatus를 찾을 수 없습니다: " + userId);
        }
    }

    private void validateUserStatusDoesNotExist(UUID userId) {
        if (findByUserIdOrNull(userId) != null) {
            throw new IllegalArgumentException("이미 존재하는 객체입니다.");
        }
    }

}
