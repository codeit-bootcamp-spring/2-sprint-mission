package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.domain.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Message extends BaseUpdatableEntity {

  @Column(columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  @Builder.Default
  private List<BinaryContent> attachments = new ArrayList<>();

  public static Message create(User author, Channel channel, String content,
      List<BinaryContent> attachments) {
    validate(author, channel);
    return Message.builder()
        .author(author)
        .channel(channel)
        .content(content)
        .attachments(attachments != null ? attachments : new ArrayList<>())
        .build();
  }

  public void update(String content) {
    if (content != null) {
      this.content = content;
    }
  }


  /*******************************
   * Validation check
   *******************************/
  private static void validate(User author, Channel channel) {
    if (author == null) {
      throw new IllegalArgumentException("작성자는 null일 수 없습니다.");
    }
    if (channel == null) {
      throw new IllegalArgumentException("채널은 null일 수 없습니다.");
    }
  }

}
