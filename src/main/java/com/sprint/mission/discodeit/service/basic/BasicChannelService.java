package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Channel.ChannelCRUDDTO;
import com.sprint.mission.discodeit.DTO.Channel.ChannelDTO;
import com.sprint.mission.discodeit.Repository.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public void reset(boolean adminAuth) {
        if (adminAuth == true) {
            channelRepository.reset();
        }
    }

    @Override
    public UUID create(ChannelDTO channelDTO) {
        ChannelCRUDDTO channelCRUDDTO = ChannelCRUDDTO.create(channelDTO.serverId(), channelDTO.creatorId(), channelDTO.name(), channelDTO.type());
        UUID serverId = channelCRUDDTO.serverId();
        UUID creatorId = channelCRUDDTO.creatorId();

        User user = userRepository.find(creatorId);
        Server findServer = serverRepository.find(serverId);
        Channel channel;

        if (channelCRUDDTO.type() == ChannelType.PRIVATE) {
            channel = new Channel(findServer.getServerId(), user.getId(),null,ChannelType.PRIVATE);
            createReadStatus(user,channel);
        } else {
            channel = new Channel(findServer.getServerId(), user.getId(), channelCRUDDTO.name());
        }

        channelRepository.save(findServer, channel);
        channelRepository.join(channel, user);

        return channel.getChannelId();
    }

    private void createReadStatus(User user, Channel channel) {
        ReadStatus readStatus = new ReadStatus(user.getId(), channel.getChannelId());
        readStatusRepository.save(readStatus);
    }


    @Override
    public UUID join(ChannelDTO channelDTO) {
        ChannelCRUDDTO channelCRUDDTO = ChannelCRUDDTO.join(channelDTO.creatorId(), channelDTO.channelId(), channelDTO.type());

        UUID userId = channelCRUDDTO.creatorId();
        UUID channelId = channelCRUDDTO.channelId();

        User user = userRepository.find(userId);
        Channel findChannel = channelRepository.find(channelId);

        UUID uuid = channelRepository.join(findChannel, user);
        if (channelCRUDDTO.type() == ChannelType.PRIVATE) {
            createReadStatus(user, findChannel);
        }

        return uuid;
    }

    @Override
    public UUID quit(ChannelDTO channelDTO) {
        ChannelCRUDDTO channelCRUDDTO = ChannelCRUDDTO.join(channelDTO.creatorId(), channelDTO.channelId(), channelDTO.type());

        UUID userId = channelCRUDDTO.creatorId();
        UUID channelId = channelCRUDDTO.channelId();

        User user = userRepository.find(userId);
        Channel findChannel = channelRepository.find(channelId);

        UUID uuid = channelRepository.quit(findChannel, user);

        return uuid;
    }

    @Override
    public ChannelDTO find(String channelId) {
        UUID channelUUID = UUID.fromString(channelId);
        Channel channel = channelRepository.find(channelUUID);
        List<Message> messageList = messageRepository.findAllByChannelId(channelUUID);
        Message message = messageList.get(messageList.size()-1);

        if (channel.getType() == ChannelType.PUBLIC) {
            ChannelDTO channelDTO = ChannelDTO.find(channel.getChannelId(), channel.getName(), null, message.createdAt);
            return channelDTO;
        } else {
            List<User> userList = channel.getUserList();
            List<UUID> userIdList = userList.stream().map(User::getId).toList();
            ChannelDTO channelDTO = ChannelDTO.find(channel.getChannelId(), channel.getName(), userIdList, message.createdAt);
            return channelDTO;
        }
    }

    @Override
    public List<ChannelDTO> findAllByServerAndUser(String serverId) {
        UUID serverUUID = UUID.fromString(serverId);
        List<Channel> channelList = channelRepository.findAllByServerId(serverUUID);
        List<ChannelDTO> findDTOList = new ArrayList<>();

        for (Channel channel : channelList) {
            ChannelDTO channelFindDTO = find(channel.getChannelId().toString());
            findDTOList.add(channelFindDTO);
        }
        return findDTOList;
    }

    @Override
    public boolean delete(ChannelDTO channelDTO) {
        ChannelCRUDDTO channelCRUDDTO = ChannelCRUDDTO.delete(channelDTO.serverId(), channelDTO.creatorId(), channelDTO.channelId());

        UUID serverId = channelCRUDDTO.serverId();
        UUID userId = channelCRUDDTO.creatorId();
        UUID channelId = channelCRUDDTO.channelId();

        Server findServer = serverRepository.find(serverId);
        Channel findChannel = channelRepository.find(channelId);

        if (findChannel.getCreatorId().equals(userId)) {
            channelRepository.remove(findServer, findChannel);

            List<Message> list = messageRepository.findAllByChannelId(findChannel.getChannelId());
            for (Message message : list) {
                messageRepository.remove(findChannel, message);
            }

            List<ReadStatus> readStatusList = readStatusRepository.findAllByChannelId(channelId);
            for (ReadStatus status : readStatusList) {
                readStatusRepository.delete(status.getReadStatusId());
            }

            return true;
        } else {
            System.out.println("채널 삭제 권한 없음");
            return false;
        }
    }

    @Override
    public boolean update(ChannelDTO channelDTO, ChannelDTO replaceDTO) {
        ChannelCRUDDTO channelIDSDTO = ChannelCRUDDTO.delete(channelDTO.serverId(), channelDTO.creatorId(), channelDTO.channelId());
        ChannelCRUDDTO channelUpdateDTO = ChannelCRUDDTO.update(replaceDTO.channelId(), replaceDTO.name(), replaceDTO.type());

        UUID serverId = channelIDSDTO.serverId();
        UUID userId = channelIDSDTO.creatorId();
        UUID channelId = channelIDSDTO.channelId();

        Channel findChannel = channelRepository.find(channelId);
        if (findChannel.getType() == ChannelType.PRIVATE) {
            System.out.println("private 채널은 수정할 수 없습니다.");
            return false;
        }

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
