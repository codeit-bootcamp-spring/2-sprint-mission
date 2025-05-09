package com.sprint.mission.discodeit.util;

public class MaskingUtil {

  public static String maskUsername(String username) {
    if (username == null) {
      return null;
    }
    if (username.length() <= 3) {
      return username.substring(0, 1) + "*".repeat(username.length() - 1);
    }
    return username.substring(0, 3) + "*".repeat(username.length() - 3);
  }

  public static String maskEmail(String email) {
    if (email == null) {
      return null;
    }
    String maskTarget;
    String[] emailArray = email.split("@");
    String domain = emailArray[1];

    if (emailArray[0].length() <= 3) {
      maskTarget = emailArray[0].substring(0, 1) + "*".repeat(emailArray[0].length() - 1);
    } else {
      maskTarget = emailArray[0].substring(0, 3) + "*".repeat(emailArray[0].length() - 3);
    }

    return maskTarget + "@" + domain;
  }
}
