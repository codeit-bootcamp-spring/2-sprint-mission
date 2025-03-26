package com.sprint.mission.discodeit.exception.binarycontent;

import java.util.UUID;

public class FileIdNotFoundException extends RuntimeException {
    public FileIdNotFoundException(UUID fileId) {
        super("해당 ID를 가진 파일을 찾을 수 없습니다 : " + fileId);
    }
}
