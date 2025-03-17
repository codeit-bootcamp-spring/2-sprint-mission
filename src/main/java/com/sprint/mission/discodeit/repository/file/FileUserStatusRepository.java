package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileUserStatusRepository extends AbstractFileRepository<UserStatus> implements UserStatusRepository {
    private Map<UUID, UserStatus> userIdMap = new HashMap<>();

    public FileUserStatusRepository() {
        super(UserStatus.class, Paths.get(System.getProperty("userStatus.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\userStatusdata"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
    }

    @Override
    public void add(UserStatus newUserStatus) {
        super.add(newUserStatus);
        super.saveToFile(directory.resolve(newUserStatus.getId().toString() + ".ser"), newUserStatus);    // file에 반영
        userIdMap.put(newUserStatus.getUserId(), newUserStatus);
    }

    @Override
    public UUID findUserStatusIDByUserId(UUID userId) {
        return userIdMap.get(userId).getId();
    }

    @Override
    public void deleteById(UUID userStatusId) {
        super.deleteById(userStatusId);
        super.deleteFile(userStatusId);
        userIdMap.remove(userStatusId);
    }
}
