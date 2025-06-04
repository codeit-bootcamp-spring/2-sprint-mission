package com.sprint.mission.discodeit.storage.s3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class S3Properties {
    // .env 내용 로드하여 사용할 때 이용
    //
    private static final Map<String, String> ENV = new HashMap<>();

    static {
        Properties props = new Properties();
        try (
                InputStream inputStream = new FileInputStream(".env")
        ) {
            props.load(inputStream);
            for (String key : props.stringPropertyNames()) {
                ENV.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return ENV.get(key);
    }
}
