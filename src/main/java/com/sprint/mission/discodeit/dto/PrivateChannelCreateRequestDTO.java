package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChannelCreateRequestDTO {
    private List<UUID> userIds; // PRIVATE 채널에 참여할 유저 ID 목록
}