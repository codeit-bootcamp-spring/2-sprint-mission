package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Channel.ChannelCreateDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelIDSDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.Repository.ChannelRepository;
import com.sprint.mission.discodeit.Repository.ServerRepository;
import com.sprint.mission.discodeit.Repository.UserRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            channelRepository.reset();
        }
    }

    @Override
    public UUID create(ChannelCreateDTO channelCreateDTO) {
        UUID serverId = UUID.fromString(channelCreateDTO.serverId());
        UUID creatorId = UUID.fromString(channelCreateDTO.creatorId());

        User user = userRepository.find(creatorId);
        Server findServer = serverRepository.find(serverId);

        Channel channel = new Channel(findServer.getServerId(), user.getId(), channelCreateDTO.name(), channelCreateDTO.type());
        channelRepository.save(findServer, channel);

        return channel.getChannelId();
    }

    @Override
    public UUID join(ChannelIDSDTO channelIDSDTO) {
        UUID userId = UUID.fromString(channelIDSDTO.userId());

        UUID channelId = UUID.fromString(channelIDSDTO.channelId());

        User user = userRepository.find(userId);
        Channel findChannel = channelRepository.find(channelId);

        UUID uuid = channelRepository.join(findChannel, user);

        return uuid;
    }

    @Override
    public UUID quit(ChannelIDSDTO channelIDSDTO) {
        UUID userId = UUID.fromString(channelIDSDTO.userId());
        UUID channelId = UUID.fromString(channelIDSDTO.channelId());

        User user = userRepository.find(userId);
        Channel findChannel = channelRepository.find(channelId);

        UUID uuid = channelRepository.quit(findChannel, user);

        return uuid;
    }

    @Override
    public boolean delete(ChannelIDSDTO channelIDSDTO) {
        UUID serverId = UUID.fromString(channelIDSDTO.serverId());
        UUID userId = UUID.fromString(channelIDSDTO.userId());
        UUID channelId = UUID.fromString(channelIDSDTO.channelId());

        Server findServer = serverRepository.find(serverId);
        Channel findChannel = channelRepository.find(channelId);

        if (findChannel.getCreatorId().equals(userId)) {
            channelRepository.remove(findServer, findChannel);
            return true;
        } else {
            System.out.println("채널 삭제 권한 없음");
            return false;
        }
    }

    @Override
    public boolean update(ChannelIDSDTO channelIDSDTO, ChannelUpdateDTO channelUpdateDTO) {
        UUID serverId = UUID.fromString(channelIDSDTO.serverId());
        UUID userId = UUID.fromString(channelIDSDTO.userId());
        UUID channelId = UUID.fromString(channelIDSDTO.channelId());

        Channel findChannel = channelRepository.find(channelId);

        if (findChannel.getCreatorId().equals(userId)) {
            channelRepository.update(findChannel, channelUpdateDTO);
            return true;
        } else {
            System.out.println("채널 수정 권한 없음");
            return false;
        }
    }

    @Override
    public boolean printChannels(String serverId) {
        UUID serverUUID = UUID.fromString(serverId);

        List<Channel> channels = channelRepository.findAllByServerId(serverUUID);
        channels.forEach(System.out::println);
        return true;
    }

    @Override
    public boolean printUsersInChannel(String channelId) {
        UUID channelUUID = UUID.fromString(channelId);
        Channel channel = channelRepository.find(channelUUID);
        List<User> list = channel.getUserList();
        list.forEach(System.out::println);
        return true;
    }
}
