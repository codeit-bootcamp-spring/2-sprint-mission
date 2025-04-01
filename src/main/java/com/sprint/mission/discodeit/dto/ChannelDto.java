package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;


//채널 DTO
public class ChannelDto {

    @Getter
    @Builder(toBuilder = true)
    public static class CreatePublic {
        @NotNull
        private UUID ownerId;
        @NotBlank
        private String channelName;
        private String description;
    }

    @Getter
    @Builder(toBuilder = true)
    public static class CreatePrivate {
        @NotNull
        private UUID ownerId;
        @NotNull
        private Set<UUID> participantIds;
        @NotBlank
        private String channelName;
    }

    @Getter
    @Builder(toBuilder = true)
    public static class Update {
        @NotNull
        private UUID channelId;
        @NotNull
        private UUID ownerId;
        private String channelName;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @NotNull
        private UUID channelId;
        @NotBlank
        private String channelName;
        @NotNull
        private UUID ownerId;
        private String description;
        @NotNull
        private String channelType;
        private ZonedDateTime lastMessageTime;
        @NotNull
        private Set<UUID> userList;
    }


    @Getter
    @Builder(toBuilder = true)
    public static class Summary {
        @NotNull
        private final UUID channelId;
        @NotBlank
        private final String channelName;
        @NotNull// 채널 이름
        private final String channelType;
        @NotNull// PRIVATE 채널 여부
        private final int participantCount;     // 참여자 수
        private final ZonedDateTime lastMessageAt; // 최근 메시지 시간
        @NotNull
        private final Set<UUID> userList; // 참여자 ID 목록

        public static Summary from(Channel channel, ReadStatus readStatus) {
            SummaryBuilder builder = Summary.builder()
                    .channelId(channel.getChannelId())
                    .channelName(channel.getChannelName())
                    .channelType(channel.getChannelType())
                    .participantCount(channel.getUserList().size())
                    ;

            // PRIVATE:참여자 목록 포함
            if (channel.isPrivate()) {
                builder.userList(channel.getUserList());
            }
            // 마지막 메시지 시간 정보 포함
            if (readStatus.getLastReadAt() != null) {
                builder.lastMessageAt(readStatus.getLastReadAt());
            }
            return builder.build();
        }
    }
    @Getter
    @Builder(toBuilder = true)
    @AllArgsConstructor
    public static class DeleteResponse {
        @NotNull
        private final  UUID channelId;
        @NotBlank
        private final  String success;

    }

}

