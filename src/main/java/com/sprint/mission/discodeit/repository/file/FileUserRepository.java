package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.NoSuchUserEmailException;
import com.sprint.mission.discodeit.exception.user.NoSuchUserNameException;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {
    // userName과 email의 빠른 중복 확인을 위한 필드
    private Map<String, User> usernames;
    private Map<String, User> emails;

    public FileUserRepository(RepositoryProperties repositoryProperties) {
        super(User.class, Paths.get(repositoryProperties.getFileDirectory()).resolve("UserData"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
        usernames = new HashMap<>();
        emails = new HashMap<>();
    }

    @Override
    public void add(User newUser) {
        super.add(newUser);                                                                 // users에 반영
        super.saveToFile(directory.resolve(newUser.getId().toString() + ".ser"), newUser);    // file에 반영
        usernames.put(newUser.getUserName(), newUser);
        emails.put(newUser.getUserEmail(), newUser);
    }

    // existsById(),findById(), getAll()  굳이 file을 탐색할 필요 없다고 생각해 storage를 통해 정보 확인, -> super.add, super.findById, super.getAll 사용

    @Override
    public void updateUserName(UUID userId, String newUserName) {
        User findUser = super.findById(userId);     // findById 에서 예외 처리
        findUser.updateUserName(newUserName);
        saveToFile(directory.resolve(userId.toString() + ".ser"), findUser);
        usernames.remove(findUser.getUserName());
        usernames.put(newUserName, findUser);
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        User findUser = super.findById(userId);     // findById 에서 예외 처리
        findUser.updateUserPassword(newPassword);
        saveToFile(directory.resolve(userId.toString() + ".ser"), findUser);
    }

    @Override
    public void updateProfileId(UUID userId, UUID newProfileId) {
        User findUser = super.findById(userId);
        findUser.updateProfileId(newProfileId);
        saveToFile(directory.resolve(userId.toString() + ".ser"), findUser);
    }

    @Override
    public boolean existsByUserName(String userName) {
        if (userName == null) {
            throw new NullPointerException("userName is null");
        }
        return usernames.containsKey(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null) {
            throw new NullPointerException("email is null");
        }
        return emails.containsKey(email);
    }

    @Override
    public User findByUserName(String userName) {
        if (!existsByUserName(userName)) {
            throw new NoSuchUserNameException(userName);
        }
        return usernames.get(userName);
    }

    @Override
    public User findByEmail(String email) {
        if (!existsByEmail(email)) {
            throw new NoSuchUserEmailException(email);
        }
        return emails.get(email);
    }

    @Override
    public void deleteById(UUID userId) {
        usernames.remove(super.findById(userId).getUserName());
        emails.remove(super.findById(userId).getUserName());
        super.deleteById(userId);                 //users에서 삭제
        super.deleteFile(userId);                 //file 삭제
    }
}
