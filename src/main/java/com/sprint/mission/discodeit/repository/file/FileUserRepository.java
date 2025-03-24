package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileUserRepository implements UserRepository {

    private final Path DIRECTORY;

    public FileUserRepository(RepositoryProperties properties) {
        this.DIRECTORY = Paths.get(properties.getUser());
        FileUtil.init(DIRECTORY);
    }

    @Override
    public UUID createUser(User user) {
        checkUserEmailExists(user.getEmail());
        checkUserNicknameExists(user.getNickname());

        return FileUtil.saveToFile(DIRECTORY, user, user.getId());
    }

    @Override
    public User findById(UUID id) {
        return (User) FileUtil.loadFromFile(DIRECTORY, id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다: " + id));
    }

    @Override
    public User findByNickname(String nickname) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof User)
                .map(object -> (User) object)
                .filter(user -> user.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 닉네임을 가진 사용자를 찾을 수 없습니다: " + nickname));
    }

    public User findByNicknameOrNull(String nickname) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof User)
                .map(object -> (User) object)
                .filter(user -> user.getNickname().equals(nickname))
                .findFirst()
                .orElse(null);
    }


    @Override
    public User findByEmail(String email) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof User)
                .map(object -> (User) object)
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 이메일을 가진 사용자를 찾을 수 없습니다: " + email));
    }

    public User findByEmailOrNull(String email) {
        return FileUtil.loadAllFiles(DIRECTORY).stream()
                .filter(object -> object instanceof User)
                .map(object -> (User) object)
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return FileUtil.loadAllFiles(DIRECTORY);
    }

    @Override
    public void updateUser(UUID id, String password, String nickname, UserStatusType status, UserRole role, UUID profileId) {
        checkUserExists(id);
        checkUserNicknameExists(nickname);
        User user = findById(id);

        user.update(password, nickname, status, role, profileId);
        FileUtil.saveToFile(DIRECTORY, user, id);
    }

    @Override
    public void deleteUser(UUID id) {
        checkUserExists(id);

        FileUtil.deleteFile(DIRECTORY, id);
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
