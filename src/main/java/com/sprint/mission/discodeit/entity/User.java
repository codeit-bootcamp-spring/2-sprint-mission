package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.entity.Util.formatTime;

import java.io.Serial;
import java.io.Serializable;

public class User extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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

    public void userUpdate(String userName, String nickName) {
        updateTime();
        this.userName = userName;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "[uid: " + id +
                ", userCreateAt: " + formatTime(getCreatedAt()) +
                ", userUpdateAt: " + (getUpdatedAt() == null ? "null" : formatTime(getUpdatedAt())) +
                ", userName: " + userName +
                ", nickName: " + nickName + "]\n";
    }

}
