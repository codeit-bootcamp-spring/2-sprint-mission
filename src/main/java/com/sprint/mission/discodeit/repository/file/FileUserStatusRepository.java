package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;

@Repository
public class FileUserStatusRepository extends AbstractFileRepository<UserStatus> implements UserStatusRepository {
    public FileUserStatusRepository() {
        super(UserStatus.class, Paths.get(System.getProperty("userStatus.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\userStatusdata"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
    }

    //
}
