package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import lombok.Getter;

@Getter
public class PrivateChannelCreateRequestDto {
    private List<User> users;
}
