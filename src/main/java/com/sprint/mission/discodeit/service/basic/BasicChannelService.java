package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.Channel.*;
import com.sprint.mission.discodeit.Repository.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public UUID create(ChannelCreateDTO channelCreateDTO) {
        UUID serverId = UUID.fromString(channelCreateDTO.serverId());
        UUID creatorId = UUID.fromString(channelCreateDTO.creatorId());

        User user = userRepository.find(creatorId);
        Server findServer = serverRepository.find(serverId);
        Channel channel;

        if (channelCreateDTO.type() == ChannelType.PRIVATE) {
            channel = new Channel(findServer.getServerId(), user.getId(),null,ChannelType.PRIVATE);
            createReadStatus(user,channel);
        } else {
            channel = new Channel(findServer.getServerId(), user.getId(), channelCreateDTO.name());
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
    public UUID join(ChannelJoinDTO channelJoinDTO) {
        UUID userId = UUID.fromString(channelJoinDTO.userId());

        UUID channelId = UUID.fromString(channelJoinDTO.channelId());

        User user = userRepository.find(userId);
        Channel findChannel = channelRepository.find(channelId);

        UUID uuid = channelRepository.join(findChannel, user);
        if (channelJoinDTO.type() == ChannelType.PRIVATE) {
            createReadStatus(user, findChannel);
        }

        return uuid;
    }

    @Override
    public UUID quit(ChannelJoinDTO channelJoinDTO) {
        UUID userId = UUID.fromString(channelJoinDTO.userId());
        UUID channelId = UUID.fromString(channelJoinDTO.channelId());

        User user = userRepository.find(userId);
        Channel findChannel = channelRepository.find(channelId);

        UUID uuid = channelRepository.quit(findChannel, user);

        return uuid;
    }

    @Override
    public ChannelFindDTO find(String channelId) {
        UUID channelUUID = UUID.fromString(channelId);
        Channel channel = channelRepository.find(channelUUID);
        List<Message> messageList = messageRepository.findAllByChannelId(channelUUID);
        Message message = messageList.get(messageList.size()-1);

        if (channel.getType() == ChannelType.PUBLIC) {
            ChannelFindDTO findDTO = createDTO(channel, message, null);
            return findDTO;
        } else {
            List<User> userList = channel.getUserList();
            List<UUID> userIdList = userList.stream().map(User::getId).toList();
            ChannelFindDTO findDTO = createDTO(channel, message, userIdList);
            return findDTO;
        }
    }

    @Override
    public List<ChannelFindDTO> findAllByServerAndUser(String serverId) {
        UUID serverUUID = UUID.fromString(serverId);
        List<Channel> channelList = channelRepository.findAllByServerId(serverUUID);
        List<ChannelFindDTO> findDTOList = new ArrayList<>();
        for (Channel channel : channelList) {
            ChannelFindDTO channelFindDTO = find(channel.getChannelId().toString());
            findDTOList.add(channelFindDTO);
        }
        return findDTOList;
    }

    private ChannelFindDTO createDTO(Channel channel, Message message, List<UUID> userIdList) {
        ChannelFindDTO channelFindDTO = new ChannelFindDTO(
                channel.getChannelId(),
                channel.getName(),
                userIdList,
                message.createdAt
        );
        return channelFindDTO;
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
    public boolean update(ChannelIDSDTO channelIDSDTO, ChannelUpdateDTO channelUpdateDTO) {
        UUID serverId = UUID.fromString(channelIDSDTO.serverId());
        UUID userId = UUID.fromString(channelIDSDTO.userId());
        UUID channelId = UUID.fromString(channelIDSDTO.channelId());

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
