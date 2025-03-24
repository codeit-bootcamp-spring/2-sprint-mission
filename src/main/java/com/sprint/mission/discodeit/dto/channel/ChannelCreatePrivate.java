package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import lombok.Getter;

@Getter
public class ChannelCreatePrivate {
    private List<User> users;
}
