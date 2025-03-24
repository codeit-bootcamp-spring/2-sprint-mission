package com.sprint.mission.discodeit.menus;

public enum UserMenu {
    CREATE("1", "유저 생성"),
    FIND("2", "유저 조회"),
    FINDALL("3", "유저 목록"),
    UPDATE("4", "유저 수정"),
    DELETE("5", "유저 삭제"),
    BACK("6", "뒤로 가기");

    private final String code;
    private final String description;

    UserMenu(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static UserMenu getByCode(String code) {
        for (UserMenu u : UserMenu.values()) {
            if (u.getCode().equals(code)) {
                return u;
            }
        }
        return null;
    }
}
