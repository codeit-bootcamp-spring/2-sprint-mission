package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Paths;

@Repository
public class FileBinaryContentRepository extends AbstractFileRepository<BinaryContent> implements BinaryContentRepository {
    public FileBinaryContentRepository() {
        super(BinaryContent.class, Paths.get(System.getProperty("BinaryContent.dir")).resolve("src\\main\\java\\com\\sprint\\mission\\discodeit\\repository\\file\\BinaryContentData"));      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
    }

    //
}
