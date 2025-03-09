package com.sprint.mission.discodeit.enums;

public enum MessageMenu {
    CREATE("1", "메세지 작성"),
    FIND("2", "메세지 조회"),
    FINDALL("3", "전체 메세지"),
    UPDATE("4", "메세지 수정"),
    DELETE("5", "메세지 삭제"),
    CHANGEDCHANNEL("6","채널 변경"),
    BACK("7", "뒤로 가기");

    private final String code;
    private final String description;

    MessageMenu(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MessageMenu getByCode(String code) {
        for (MessageMenu m : MessageMenu.values()) {
            if (m.getCode().equals(code)) {
                return m;
            }
        }
        return null;
    }
}
