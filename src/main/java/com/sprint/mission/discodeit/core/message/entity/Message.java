package com.sprint.mission.discodeit.core.message.entity;

import com.sprint.mission.discodeit.core.BaseUpdatableEntity;
import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.message.exception.MessageInvalidRequestException;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "messages")
@Entity
public class Message extends BaseUpdatableEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private User author;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", nullable = false)
  private Channel channel;

  @Column(columnDefinition = "text")
  private String content;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "message_attachments",
      joinColumns = @JoinColumn(name = "message_id"),
      inverseJoinColumns = @JoinColumn(name = "attachment_id")
  )
  private List<BinaryContent> attachment;

  private Message(User author, Channel channel, String content, List<BinaryContent> attachment) {
    super();

    this.author = author;
    this.channel = channel;

    this.content = content;
    this.attachment = attachment;
  }

  public static Message create(User user, Channel channel, String text,
      List<BinaryContent> attachmentIds) {
    Validator.validate(text);
    return new Message(user, channel, text, attachmentIds);
  }

  public void update(String newText) {
    if (newText != null && !newText.equals(content)) {
      //정규패턴 적용할 때 유효성 검증 목적으로 사용
      Validator.validateText(newText);
      this.content = newText;
    }
  }

  public static class Validator {

    public static void validate(String text) {
      validateText(text);
    }

    //TODO 정규 패턴 적용 예정 => 욕설 문자 등등 정규패턴으로 잡아버리기
    public static void validateText(String text) {
      if (text == null) {
        throw new MessageInvalidRequestException(ErrorCode.MESSAGE_INVALID_REQUEST);
      }
    }
  }
}
