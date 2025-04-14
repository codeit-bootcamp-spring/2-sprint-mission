package com.sprint.discodeit.sprint.service.basic.chnnel;

import com.sprint.discodeit.sprint.domain.ChannelType;
import com.sprint.discodeit.sprint.domain.dto.channelDto.ChannelFindResponseDto;
import com.sprint.discodeit.sprint.domain.entity.Channel;
import com.sprint.discodeit.sprint.repository.file.FileChannelRepository;
import com.sprint.discodeit.sprint.service.basic.userss.ReadStatusService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelQueryService {

    private final FileChannelRepository filechannelRepository;
    private final ReadStatusService readStatusService;

    public ChannelFindResponseDto find(UUID channelId) {
        Channel channel = filechannelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다: " + channelId));

        //Instant lastMessageTime = messageService.getLastMessageTime(channelId);
        // TODO : 임시로 현재 시간으로 나중에 메세지 시간으로 변경
        Instant lastMessageTime = Instant.now();
        List<UUID> usersIds = new ArrayList<>(); // 기본값 빈 리스트

        if(channel.getType() == ChannelType.PRIVATE) {
            usersIds = readStatusService.getusersIdsByChannel(channelId);
        }

        return new ChannelFindResponseDto(
                channel.getId(),
                channel.getName(),
                lastMessageTime,
                channel.getType(),
                usersIds
        );
    }

    /**
     * 특정 유저가 볼 수 있는 채널 목록 조회 (PUBLIC 전체 조회, PRIVATE은 참여한 채널만)
     */
    public List<ChannelFindResponseDto> findAllByusersId() {
        List<Channel> channels = filechannelRepository.findByAll();
        List<ChannelFindResponseDto> responseList = new ArrayList<>();

        for (Channel channel : channels) {
            // PUBLIC 채널은 모든 사용자가 볼 수 있음
            // PRIVATE 채널은 사용자가 참여한 경우에만 볼 수 있음
            if (channel.getType() == ChannelType.PUBLIC) {
                Instant lastMessageTime = Instant.now(); // TODO: 메시지의 시간으로 변경
                List<UUID> usersIds = new ArrayList<>();

                if (channel.getType() == ChannelType.PRIVATE) {
                    usersIds = readStatusService.getusersIdsByChannel(channel.getId());
                }

                responseList.add(new ChannelFindResponseDto(
                        channel.getId(),
                        channel.getName(),
                        lastMessageTime,
                        channel.getType(),
                        usersIds
                ));
            }
        }

        return responseList;
    }
}
