package com.sprint.mission.discodeit.menus;

import lombok.Getter;

@Getter
public enum MainMenu {
    USER("1", "유저 관리"),
    CHANNEL("2", "채널 관리"),
    MESSAGE("3", "메세지 시작"),
    EXIT("4", "종료");

    private final String code;
    private final String description;

    MainMenu(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MainMenu getByCode(String code) {
        for (MainMenu menu : MainMenu.values()) {
            if (menu.getCode().equals(code)) {
                return menu;
            }
        }

        return null;
    }
}
