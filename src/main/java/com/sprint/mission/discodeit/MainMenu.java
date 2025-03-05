package com.sprint.mission.discodeit;

public enum MainMenu {
    USER_MANAGEMENT(1),
    CHANNEL_MANAGEMENT(2),
    MESSAGE_MANAGEMENT(3),
    EXIT(0);

    private final int value;

    MainMenu(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MainMenu from(int value) {
        for (MainMenu menu : values()) {
            if (menu.value == value) {
                return menu;
            }
        }
        throw new IllegalArgumentException("잘못된 입력입니다: " + value);
    }
}
