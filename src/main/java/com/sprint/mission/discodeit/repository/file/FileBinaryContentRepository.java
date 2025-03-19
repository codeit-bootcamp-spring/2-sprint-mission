package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class FileBinaryContentRepository {
    private final Path PROFILE_DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map",
            BinaryContent.class.getSimpleName(), "Profile");
    private final Path ATTACHMENT_DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map",
            BinaryContent.class.getSimpleName(), "Attatchment");

    public FileBinaryContentRepository() {
        FileUtil.init(PROFILE_DIRECTORY);
        FileUtil.init(ATTACHMENT_DIRECTORY);
    }

    private Path FileTypeDetector(BinaryContent binaryContent) {
        if (binaryContent.getType().equals(BinaryContentType.USER_PROFILE)) {
            return PROFILE_DIRECTORY;
        }
        return ATTACHMENT_DIRECTORY;
    }

    public BinaryContent save(BinaryContent binaryContent, MultipartFile file) {
        // 1. 메타데이터 저장. 2. 실제 파일데이터 저장
        Path directory = FileTypeDetector(binaryContent);

    }


}
