package com.sprint.mission.discodeit.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatter {
    public static String formatTimestamp(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timeStamp));
    }
}
