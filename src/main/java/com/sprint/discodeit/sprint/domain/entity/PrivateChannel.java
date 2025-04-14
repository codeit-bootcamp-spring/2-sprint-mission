package com.sprint.discodeit.sprint.domain.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PrivateChannel extends Channel {


    //TODO : 나중 Jwt 반영하면 나중에 작업
//    // 채널이 잠금 상태인지 여부 (true이면 초대받은 유저만 입장 가능)
//    private boolean locked;
//
//    // 비밀 채널 초대용 코드 (링크 또는 숫자 형태로 사용 가능)
//    private String inviteCode;
}
