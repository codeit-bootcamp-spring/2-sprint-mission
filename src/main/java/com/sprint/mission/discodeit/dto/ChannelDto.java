package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.ChannelType;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.context.event.EventListener;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.Duration;

//채널 DTO
public class ChannelDto {

    @Getter
    @Builder(toBuilder = true)
    public static class CreatePublic {
        @NotBlank
        private String channelName;
        private UUID ownerId;
        private String description;
    }
    @Getter
    @Builder(toBuilder = true)
    public static class CreatePrivate {
        private UUID ownerId;
        private Set<UUID> participantIds;
        private String channelName; // 선택적 필드
    }
    @Getter
    @Builder(toBuilder = true)
    public static class Update {
        private UUID channelId;
        private String channelName;
        private String description;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private UUID channelId;
        private String channelName;
        private UUID ownerId;
        private String description;
        private boolean isPrivate;
        private Set<UUID> userList;
        private ZonedDateTime lastMessageTime; // 추가: 최근 메시지 시간
    }

    //간단한 조회용
    @Getter
    @Builder(toBuilder = true)
    public static class Summary {
        @NotBlank()
        private final UUID id;
        private final String Name;       // 채널 이름
        private final boolean isPrivate;        // PRIVATE 채널 여부
        private final int participantCount;     // 참여자 수
        private final ZonedDateTime lastMessageAt; // 최근 메시지 시간
        private final Set<UUID> participantIds; // 참여자 ID 목록 (private)

        public static Summary from(Channel channel, ReadStatus readStatus) {
            SummaryBuilder builder = Summary.builder()
                    .id(channel.getChannelId())
                    .Name(channel.getChannelName())
                    .isPrivate(channel.isPrivate())
                    .participantCount(channel.getUserList().size());

            // PRIVATE 채널인 경우 참여자 목록 포함
            if (channel.isPrivate()) {
                builder.participantIds(channel.getUserList());
            }
            // 마지막 메시지 시간 정보 포함
            if (readStatus.getLastReadAt() != null) {
                builder.lastMessageAt(readStatus.getLastReadAt());
            }
            return builder.build();
        }
    }


}