package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "messages")
@Getter
public class Message extends BaseUpdatableEntity {

  //@Lob DB종류에 따라 자동으로 매핑     // @Column(columnDefinition = "TEXT") : 지정된 문자열 타입으로 맵핑, 하지만 특정 DB에 의존적
  @Column(name = "content", nullable = true, columnDefinition = "TEXT")
  private String content;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "author_id")
  private User author;

  @OneToMany
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachments;

  public Message() {
    super();
  }

  public Message(String content, Channel channel, User author, List<BinaryContent> attachments) {
    super();
    this.content = content;
    this.channel = channel;
    this.author = author;
    this.attachments = attachments;
  }

  public void update(String newContent) {
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
    }
  }
}
