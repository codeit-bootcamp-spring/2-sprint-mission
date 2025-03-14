package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;

@Repository
public class FileReadstatusRepository extends AbstractFileRepository<ReadStatus> implements ReadStatusRepository {
    public FileReadstatusRepository() {
        super(ReadStatus.class, Paths.get(System.getProperty("userStatus.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\readStatusdata"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
    }
}
