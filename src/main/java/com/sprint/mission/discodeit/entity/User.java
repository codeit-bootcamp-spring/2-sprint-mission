package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public class User extends BaseEntity {

    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }



    public void update(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        super.update();
        System.out.printf("[ %s ], [ %s ], [ %s ] 로 변경되었습니다.", this.name, this.email, this.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof User user) {
            return user.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nName: " + name + "\nMail: " + email + "\nPassword: " + password +
                "\nUUID: " + this.getId() +
                "\nCreatedAt: " + this.getCreatedAtFormatted() +
                "\nUpdatedAt: " + this.getUpdatedAttFormatted();

    }
}
