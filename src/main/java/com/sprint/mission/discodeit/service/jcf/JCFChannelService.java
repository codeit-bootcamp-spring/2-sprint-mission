package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.infra.ChannelRepository;
import com.sprint.mission.discodeit.infra.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private static final JCFChannelService jcfChannelService = new JCFChannelService();
    private static final ChannelRepository channelRepository = JCFChannelRepository.getInstance();
    private static final UserService userService = JCFUserService.getInstance();

    private JCFChannelService() {
    }

    public static JCFChannelService getInstance() {
        return jcfChannelService;
    }

    @Override
    public ChannelDto create(String name, UserDto owner) {
        Channel channel = channelRepository.save(
                new Channel(name, owner.id())
        );

        List<UserDto> users = channel.getUserIds()
                .stream()
                .map(userId -> new UserDto(userId, userService.findById(userId).name()))
                .toList();

        return new ChannelDto(channel.getId(), channel.getName(), users);
    }

    @Override
    public ChannelDto findById(UUID id) {
        Channel channel = channelRepository.findById(id);

        List<UserDto> users = channel.getUserIds()
                .stream()
                .map(userId -> new UserDto(userId, userService.findById(userId).name()))
                .toList();

        return new ChannelDto(channel.getId(), channel.getName(), users);
    }

    @Override
    public List<ChannelDto> findAll() {
        List<Channel> channels = channelRepository.findAll();
        Map<UUID, List<UserDto>> channelUsers = new HashMap<>();

        for (Channel channel : channels){
            List<UserDto> users = channel.getUserIds()
                    .stream()
                    .map(id -> new UserDto(id, userService.findById(id).name()))
                    .toList();

            channelUsers.put(channel.getId(), users);
        }

        return channels
                .stream()
                .map(channel -> new ChannelDto(channel.getId(), channel.getName(), channelUsers.get(channel.getId())))
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        channelRepository.updateName(id, name);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }

    @Override
    public ChannelDto addMember(UUID id, String email) {
        Channel channel = channelRepository.findById(id);
        UserDto user = userService.findByEmail(email);
        channel.addMember(user.id());

        List<UserDto> users = channel.getUserIds()
                .stream()
                .map(userId -> new UserDto(userId, userService.findById(userId).name()))
                .toList();

        return new ChannelDto(channel.getId(), channel.getName(), users);
    }
}
