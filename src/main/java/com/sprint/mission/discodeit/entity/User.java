package com.sprint.mission.discodeit.entity;

public class User extends Domain{
    private static int count;
    private String password;
    private UserStatus userStatus;

//    나중에 추가할 기능
//    private String email;
//    private String birthday;

    public User(){
        this("U" + count++, null);
    }

    public User(String id, String name) {
        super(id, name);
    }
}
