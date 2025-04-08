package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/*
@Builder를 사용하면 자동으로 빌더 패턴이 적용된 생성자가 생성됨! // 아예 생성자를 작성할 필요가 없음.
// 기존 방식은 생성자 호출 생성, 빌더 패턴은 객체 직접 생성, 높은 가독성과 많은 매개변수로 인한 실수 가능성이 낮아짐.
@Getter 추가해서 getId(), getName() 등 Getter 메서드 자동 생성, 코드로 작성할 필요 없어짐.
id, createdAt은 final로 선언해서 불변 객체 유지
 */

@Getter
//@Builder
public class Channel implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final Instant createdAt; //Long -> Instant로 변경, 가독성이 뛰어나며, 시간대(Time Zone) 변환과 정밀한 시간 연산이 가능해 확장성이 높습니다.
  private Instant updatedAt;

  private ChannelType type;
  private String name;
  private String description;

  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();

    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    boolean anyValueUpdated = false;
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      anyValueUpdated = true;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
            /*
            - UTC는 세계 표준 시간, 한국(KST)은 UTC+9
            - Instant 객체는 "날짜 + 시간 (나노초 단위까지 포함)"을 가집니다.
            - .now()는 Instant 객체를 반환하며 년-월-일T시:분:초.나노초Z 형식으로 출력됨.
            ex: Instant.now(): 2025-03-18T08:30:00.123456Z
             */
      this.updatedAt = Instant.now();
    }
  }

  @Override
  public String toString() {
    return "Channel{" +
        "id=" + id +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", type=" + type +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
