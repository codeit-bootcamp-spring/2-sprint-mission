package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public UUID createUser(User user) {
        checkUserEmailExists(user.getEmail());
        checkUserNicknameExists(user.getNickname());

        data.put(user.getId(), user);
        return findById(user.getId()).getId();
    }

    @Override
    public User findById(UUID id) {
        User user = data.get(id);
        if(user == null){
            throw new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + id);
        }
        return user;
    }

    @Override
    public User findByNickname(String nickname) {
        return data.values().stream()
                .filter(user -> user.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 닉네임을 가진 사용자를 찾을 수 없습니다: " + nickname));
    }

    public User findByNicknameOrNull(String nickname) {
        return data.values().stream()
                .filter(user -> user.getNickname().equals(nickname))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return data.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 이메일을 가진 사용자를 찾을 수 없습니다: " + email));
    }

    public User findByEmailOrNull(String email) {
        return data.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, String password, String nickname, UserStatusType status, UserRole role, UUID profileId) {
        checkUserExists(id);
        checkUserNicknameExists(nickname);
        User user = data.get(id);

        user.update(password, nickname, status, role, profileId);
    }

    @Override
    public void deleteUser(UUID id) {
        checkUserExists(id);

        data.remove(id);
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkUserExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + id);
        }
    }

    private void checkUserEmailExists(String email) {
        if (findByEmailOrNull(email) != null) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }

    private void checkUserNicknameExists(String nickname) {
        if (findByNicknameOrNull(nickname) != null) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
    }

}
