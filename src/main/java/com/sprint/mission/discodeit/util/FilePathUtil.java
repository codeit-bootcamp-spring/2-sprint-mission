package com.sprint.mission.discodeit.util;

import java.nio.file.Path;
import java.util.UUID;

public class FilePathUtil {
    public static Path getFilePath(Path directory, UUID id) {

        return directory.resolve(id.toString().concat(".ser"));
    }
}
