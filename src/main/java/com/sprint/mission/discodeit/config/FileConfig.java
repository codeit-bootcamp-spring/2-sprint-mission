package com.sprint.mission.discodeit.config;

import java.util.HashSet;
import java.util.Set;

public class FileConfig {
    public static Set<String> ALLOWED_EXTENSIONS = new HashSet<>();

    static {
        ALLOWED_EXTENSIONS.add(".jpg");
        ALLOWED_EXTENSIONS.add(".png");
        ALLOWED_EXTENSIONS.add(".gif");
        ALLOWED_EXTENSIONS.add(".pdf");
        ALLOWED_EXTENSIONS.add(".txt");
    }
}
