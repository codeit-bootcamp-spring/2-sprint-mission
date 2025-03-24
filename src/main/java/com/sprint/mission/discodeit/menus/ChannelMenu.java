package com.sprint.mission.discodeit.menus;

public enum ChannelMenu {
    CREATE("1", "채널 생성"),
    FIND("2", "채널 조회"),
    FINDALL("3", "채널 목록"),
    UPDATE("4", "채널 수정"),
    DELETE("5", "채널 삭제"),
    BACK("6", "뒤로 가기");

    private final String code;
    private final String description;

    ChannelMenu(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ChannelMenu getByCode(String code) {
        for (ChannelMenu c : ChannelMenu.values()) {
            if (c.getCode().equals(code)) {
                return c;
            }
        }
        return null;
    }
}
