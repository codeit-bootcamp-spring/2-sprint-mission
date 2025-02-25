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

    public void updateUser(String name, String email) {
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
}
