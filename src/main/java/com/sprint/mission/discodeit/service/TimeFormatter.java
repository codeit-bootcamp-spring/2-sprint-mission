package com.sprint.mission.discodeit.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class TimeFormatter {
    public static String formatTimestamp(Instant timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Date.from(timeStamp));
    }
}
