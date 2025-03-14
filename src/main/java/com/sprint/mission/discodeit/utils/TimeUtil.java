package com.sprint.mission.discodeit.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static String convertToFormattedDate(Long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp); // Long을 Instant로 변환
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")  // 원하는 형식으로 지정
                .withZone(ZoneId.of("Asia/Seoul")); // 한국 기준으로 출력
        return formatter.format(instant);
    }
}
