package com.sprint.mission.discodeit.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LogMapUtil {
    private final Map<String, Object> log = new LinkedHashMap<>();

    private LogMapUtil(String key, Object value) {
        log.put(key, value);
    }

    public static LogMapUtil of(String key, Object value) {
        return new LogMapUtil(key, value);
    }

    public LogMapUtil add(String key, Object value) {
        log.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return log.toString();
    }
}
