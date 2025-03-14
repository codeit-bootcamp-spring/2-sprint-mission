package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String profile;

    public void updateNickname(String nickname) {
        super.updateTime();
        this.nickname = nickname;
    }
}
