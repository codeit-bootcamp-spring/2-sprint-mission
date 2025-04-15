package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor
public class Message extends BaseUpdatableEntity implements Serializable, Identifiable {

  private static final long serialVersionUID = 1L;

  //  @ManyToMany
  //  @JoinTable(
  //      name = "message_attachments",
  //      joinColumns = @JoinColumn(name = "message_id"),
  //      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  //  )
  // 이것보단 연결 엔티티로 만들어 관계를 풀어내는 것이 좋다.
  // 미션에선 연결 테이블에 추가 컬럼 확장 가능성이 없으므로 @JoinTable을 사용?? - 다음 미션에서 봐야할듯
  @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MessageAttachment> attachments;

  @Lob
  private String content;

  @ManyToOne
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private User author;

  @Builder
  public Message(String content, Channel channel, User author,
      List<MessageAttachment> attachments) {
    this.attachments = attachments != null ? new ArrayList<>(attachments) : new ArrayList<>();
    this.content = content;
    this.channel = channel;
    this.author = author;
  }

  public void updateMessageInfo(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }

  public void updateMessageAttachment(List<MessageAttachment> attachments) {
    this.attachments = new ArrayList<>(attachments);
  }

}
