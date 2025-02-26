package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {

    //
    private String name;
    private String email;


    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
        super.update();
    }


    @ Override
    public boolean equals(Object object) {
        if (object instanceof User user) {
            return user.getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public String toString() {
        return "이름: " + name + "\n메일: " + email + "\n사용자 ID: " + this.getId() +
                "\n생성 시간:" + this.getCreatedAtFormatted() + "\n업데이트 시간: " + this.getupdatedAttFormatted();
    }
}
