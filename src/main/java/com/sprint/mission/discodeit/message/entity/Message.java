package com.sprint.mission.discodeit.message.entity;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.common.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Table(name = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", columnDefinition = "uuid")
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", columnDefinition = "uuid")
    private User user;

    @Column(name = "content", nullable = false)
    private String context;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BinaryContent> attachments;

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
