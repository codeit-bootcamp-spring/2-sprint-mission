package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;

@Getter
@EntityScan
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 101L;
    private final UUID id;
    private final ZonedDateTime createdAt; // 객체 생성 시간
    private ZonedDateTime updateAt;
    //가입 채널 리스트
    private final Set<UUID> belongChannels = new HashSet<>();
    private String email;
    private String password;
    private UUID profileId;

    // 이메일과 비밀번호
    public User(String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt =ZonedDateTime.now(); // 생성시간
        this.updateAt = null;
        this.email = email;
        this.password = password;
    }
    // 변경 시 호출
    public void setUpdateAt() {
        this.updateAt = ZonedDateTime.now();
    }

    // 채널 참여
    public void addBelongChannel(UUID channelId) {
        belongChannels.add(channelId);
    }

    // 채널 탈퇴
    public void removeBelongChannel(UUID channelId) {
        belongChannels.remove(channelId);
    }
    public void setEmail(String email) {
        this.email = email;
        setUpdateAt(); // 이메일 변경 시 업데이트 시간 갱신
    }
    // 비밀번호 변경 시
    public void setPassword(String password) {
        this.password = password;
        setUpdateAt(); // 비밀번호 변경 시 업데이트 시간 갱신
    }
    //로그인
    public boolean checkPassword(String inputPassword) {return this.password != null && this.password.equals(inputPassword);}

    // 프로필 이미지 id만 가지면 됨
    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
        setUpdateAt(); // 프로필 이미지 변경 시 업데이트 시간 갱신
    }
}