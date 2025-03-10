package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {

    private String name;
    private String email;

    public User(String name, String email) {
        super();
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
        System.out.printf("[ %s ], [ %s ] 로 변경되었습니다.", this.name, this.email);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof User user) {
            return user.getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nName: " + name + "\nMail: " + email +
                "\nUUID: " + this.getId() +
                "\nCreatedAt: " + this.getCreatedAtFormatted() +
                "\nUpdatedAt: " + this.getUpdatedAttFormatted();

    }
}
