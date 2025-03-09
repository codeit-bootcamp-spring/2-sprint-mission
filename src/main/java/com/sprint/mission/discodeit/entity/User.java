package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 101L;

    // 유저아이디
    private final UUID userId;
    private final Long createdAt; // 객체 생성 시간
    private Long updateAt; 
    private final Set<UUID> belongChannels = new HashSet<>();
    
    // 이메일과 비밀번호
    private String email;
    private String password;

    // 이메일과 비밀번호를 받는 생성자
    public User(String email, String password) {
        this.userId = UUID.randomUUID(); // 랜덤 uuid생성
        this.createdAt = new Date().getTime(); // 생성시간
        this.updateAt = null;
        this.email = email;
        this.password = password;
    }

    public UUID getId() {
        return userId;
    }

    // 기존 메서드와의 호환성을 위해 남겨둠
    @Deprecated
    public UUID getUUIDId() {
        return getId();
    }

    // 변경 시 호출
    public void setUpdateAt() {
        this.updateAt = new Date().getTime();
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    // 유저가 속한 채널
    public Set<UUID> getBelongChannels() {
        return belongChannels;
    }

    // 채널 참여
    public void addBelongChannel(UUID channelId) {
        belongChannels.add(channelId);
    }

    // 채널 탈퇴
    public void removeBelongChannel(UUID channelId) {
        belongChannels.remove(channelId);
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
        setUpdateAt(new Date().getTime()); // 이메일 변경 시 업데이트 시간 갱신
    }
    
    // 비밀번호 관련 메소드
    public void setPassword(String password) {
        this.password = password;
        setUpdateAt(new Date().getTime()); // 비밀번호 변경 시 업데이트 시간 갱신
    }
    
    // 비밀번호 확인
    public boolean checkPassword(String inputPassword) {
        return this.password != null && this.password.equals(inputPassword);
    }
    public boolean checkEmail(String inputEmail) {
        return this.email != null && this.email.equals(inputEmail);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", email=" + email +
                ", createdAt=" + createdAt +
                ", updateAt=" + updateAt +
                ", Channel='" + belongChannels + '\'' +
                '}';
    }
}