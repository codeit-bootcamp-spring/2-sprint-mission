package com.sprint.mission.discodeit.dto.channel;


import jakarta.validation.constraints.Size;

public record UpdateChannelRequest(
    @Size(max = 30, message = "채널 이름은 최대 30자까지 입력할 수 있습니다.")
    String newName,

    @Size(max = 255, message = "채널 설명은 최대 255자까지 입력할 수 있습니다.")
    String newDescription
) {

}
