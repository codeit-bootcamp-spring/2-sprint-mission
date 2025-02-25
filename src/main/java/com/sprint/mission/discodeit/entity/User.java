package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String userName;
    private String nickName;

    public User(String userName, String nickName) {
        super();
        this.userName = userName;
        this.nickName = nickName;
    }


    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void userUpdate(String userName, String nickName){
        updateTime();
        this.userName = userName;
        this.nickName = nickName;
    }

    @Override
    public String toString(){
        return "[uid: " + id +
                ", userCreateAt: " + formatTime(getCreateAt()) +
                ", userUpdateAt: " + (getUpdateAt() == null ? "null" : formatTime(getUpdateAt())) +
                ", userName: " + userName +
                ", nickName: " + nickName + "]\n";
    }

}
