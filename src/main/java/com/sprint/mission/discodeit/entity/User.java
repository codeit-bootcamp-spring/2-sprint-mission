package com.sprint.mission.discodeit.entity;

import java.util.*;
import java.util.UUID;

public class User {
    private UUID id;  //유저의 아이디
    private Long cratedAt; //객체 생성 시간
    private String userid; //유저아이디
    private Long updateAt;//정보 업데이트시 (id,at) 둘다 바뀜
    private List<String> belongChannels = new ArrayList<>();
    public User(String userid) {
        this.id = UUID.randomUUID();//랜덤 uuid생성
        this.userid=userid;//uuid:지정 아이디 매핑
        this.cratedAt = new Date().getTime(); //생성시간
    }

    public String getuserId() {
        return userid; //유저는 uuid 알 필요 x
    }
    public UUID getUUIDId() {
       return id;
    }
    public void setupdatedAt() {
        this.updateAt = new Date().getTime();
    }
    public Long getCratedAt() {
        return cratedAt;
    }
    public Long getUpdateAt() {
        return updateAt;
    }

    public void setbelongchannel(String belongchannel) {
        belongChannels.add(belongchannel);
    }
    public void UpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }

    public List<String> getBelongChannels() {
        return belongChannels;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public void addBelongChannel(String channel) {
        belongChannels.add(channel);
    }

    public void removeBelongChannel(String channel) {
        belongChannels.remove(channel);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + userid +
                ", cratedAt=" + cratedAt +
                ", updateAt=" + updateAt+
                ", Channel='" + belongChannels+ '\'' +
                '}';
    }
}
