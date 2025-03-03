package com.sprint.mission.discodeit.config;

public enum SetUpUserInfo {
    LONGIN_USER("황지환", "hwang@naver.com", "12345"),
    OTHER_USER("박지환", "park@naver.com", "12345");

    private String name;
    private String email;
    private String password;

    SetUpUserInfo(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
