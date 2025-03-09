package com.sprint.mission.discodeit.menu;

public enum ChannelMenu {
    CREATE_CHANNEL(1),
    GET_CHANNEL(2),
    UPDATE_CHANNEL(3),
    DELETE_CHANNEL(4),
    BACK(0);

    private final int value;

    ChannelMenu(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ChannelMenu from(int value) {
        for (ChannelMenu menu : values()) {
            if (menu.value == value) {
                return menu;
            }
        }
        throw new IllegalArgumentException("잘못된 입력입니다: " + value);
    }
}
