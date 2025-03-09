package com.sprint.mission.discodeit.menu;

public enum UserMenu {
    CREATE_USER(1),
    GET_USER(2),
    BACK(0);

    private final int value;

    UserMenu(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserMenu from(int value) {
        for (UserMenu menu : values()) {
            if (menu.value == value) {
                return menu;
            }
        }
        throw new IllegalArgumentException("잘못된 입력입니다: " + value);
    }
}
