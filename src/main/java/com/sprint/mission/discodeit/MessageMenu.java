package com.sprint.mission.discodeit;

public enum MessageMenu {
    CREATE_MESSAGE(1),
    GET_MESSAGE(2),
    UPDATE_MESSAGE(3),
    DELETE_MESSAGE(4);

    private final int value;

    MessageMenu(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MessageMenu from(int value) {
        for (MessageMenu menu : values()) {
            if (menu.value == value) {
                return menu;
            }
        }
        throw new IllegalArgumentException("잘못된 입력입니다: " + value);
    }
}
