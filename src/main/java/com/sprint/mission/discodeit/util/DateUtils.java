package com.sprint.mission.discodeit.util;

import java.text.SimpleDateFormat;
import java.time.Instant;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss.SS");

    public static String toStrData(Instant time) {
        return DATE_FORMAT.format(time);
    }
}
