package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.UUID;

@Repository
public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {
    private static volatile FileUserRepository instance;         // volatile을 사용하여 변수의 값을 JVM이 캐시하지 않도록 보장

    private FileUserRepository() {
        super(User.class, Paths.get(System.getProperty("user.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\userdata"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
    }

    public static FileUserRepository getInstance() {
        // 첫 번째 null 체크 (성능 최적화)
        if (instance == null) {
            synchronized (FileUserRepository.class) {
                // 두 번째 null 체크 (동기화 구간 안에서 중복 생성 방지)
                if (instance == null) {
                    instance = new FileUserRepository();
                }
            }
        }
        return instance;
    }

    @Override
    public void add(User newUser) {
        super.add(newUser);                                                                 // users에 반영
        super.saveToFile(directory.resolve(newUser.getId().toString() + ".ser"), newUser);    // file에 반영
    }

    // existsById(),findById(), getAll()  굳이 file을 탐색할 필요 없다고 생각해 storage를 통해 정보 확인, -> super.add, super.findById, super.getAll 사용

    @Override
    public void deleteById(UUID userId) {
        super.deleteById(userId);                 //users에서 삭제
        super.deleteFile(userId);                 //file 삭제
    }

    @Override
    public void updateUserName(UUID userId, String userName) {
        if (existsById(userId)) {
            super.storage.get(userId).updateUserName(userName);
        }
        saveToFile(directory.resolve(userId.toString() + ".ser"), super.storage.get(userId));
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        if (existsById(userId)) {
            super.storage.get(userId).updateUserPassword(newPassword);
        }
        saveToFile(directory.resolve(userId.toString() + ".ser"), super.storage.get(userId));
    }
}
