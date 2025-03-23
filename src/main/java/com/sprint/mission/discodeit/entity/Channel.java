package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String channelName;
    protected final UUID id;
    protected final Instant createAt;
    protected Instant updateAt;
    private boolean isPublic;
    private Set<User> participants = new HashSet<>();

    public Channel() {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = createAt;
        this.channelName = "Unknown";
        this.isPublic = true;
    }

    public Channel(String channelName, boolean isPublic) {
        this();
        this.channelName = channelName;
        this.isPublic = isPublic;
    }

    public void updateChannelName(String newChannelName) {
        this.channelName = newChannelName;
        this.updateAt = Instant.now();
    }
}
