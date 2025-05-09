package com.sprint.mission.discodeit.message.entity;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.common.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Entity
@Table(name = "messages")
public class Message extends BaseUpdatableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User user;

    @Column(name = "content")
    private String context;

    @ManyToMany
    @JoinTable(
            name = "message_attachments",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id")
    )
    private List<BinaryContent> attachments;

    protected Message() {
    }

    public Message(Channel channel, User user, String context, List<BinaryContent> attachments) {
        this.channel = channel;
        this.user = user;
        this.context = context;
        this.attachments = attachments;
    }

    public void updateContext(String context) {
        this.context = context;
    }

}
