package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.ChannelType;
import lombok.Getter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

  @Serial
  private static final long serialVersionUID = 102L;
  private final UUID channelId; // 채널 ID
  private final ZonedDateTime createdAt; // 생성시간
  private ZonedDateTime updateAt; // 업데이트 된 시간
  private final UUID ownerId; // 채널 생성자의 아이디
  private final Set<UUID> userList = new HashSet<>(); // 가입한 유저 리스트
  private String channelName; //채널이름
  private final String channelType; // 채널 타입 (PUBLIC/PRIVATE)
  private String description; // 채널 설명 (PUBLIC)

  // PUBLIC
  public Channel(String channelName, UUID ownerId) {
    this.createdAt = ZonedDateTime.now();
    this.channelId = UUID.randomUUID();
    this.channelName = channelName;
    this.ownerId = ownerId;
    this.channelType = ChannelType.PUBLIC;
    userList.add(ownerId); // 생성자를 채널에 자동 추가
  }

  // PUBLIC
  public Channel(String channelName, UUID ownerId, String description) {
    this(channelName, ownerId);
    this.description = description;
  }

  // PRIVATE 채널 생성자
  public Channel(UUID ownerId, Set<UUID> participants) {
    this.createdAt = ZonedDateTime.now();
    this.channelId = UUID.randomUUID();
    this.ownerId = ownerId;
    this.channelType = ChannelType.PRIVATE;

    // 참여자 추가
    userList.add(ownerId);
    if (participants != null) {
      userList.addAll(participants);
    }
  }

  // PRIVATE 채널 생성자 (이름 포함)
  public Channel(String channelName, UUID ownerId, Set<UUID> participants) {
    this(ownerId, participants);
    this.channelName = channelName;
  }

  // 채널 참여
  public void joinChannel(UUID userId) {
    this.getUserList().add(userId);
  }

  // 채널 탈퇴
  public void leaveChannel(UUID userId) {
    this.getUserList().remove(userId);
  }

  // 채널명 변경
  public void setChannelName(String newChannelName) {
    this.channelName = newChannelName;
    setUpdateAt();
  }

  // 설명 변경 (PUBLIC)
  public void setDescription(String description) {
    if (isPublic()) {
      this.description = description;
      setUpdateAt();
    }
  }

  // 업데이트 시간 설정
  public void setUpdateAt() {
    updateAt = ZonedDateTime.now();
  }

  //    // 채널 타입 확인 메서드
  public boolean isPrivate() {
    return ChannelType.PRIVATE.equals(this.channelType);
  }

  public boolean isPublic() {
    return ChannelType.PUBLIC.equals(this.channelType);
  }


  public UUID getId() {
    return this.channelId;
  }

  public String getName() {
    return this.channelName;
  }
}