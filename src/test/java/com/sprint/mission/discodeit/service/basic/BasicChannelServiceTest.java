package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.Test;

class BasicChannelServiceTest {
    ChannelRepository channelRepository = new JCFChannelRepository();
    ChannelService channelService = BasicChannelService.getInstance(channelRepository);;

    @Test
    void testBasicChannelService() {
        Channel testChannel = new Channel("Test Channel");
        Channel testChannel2 = new Channel("Test Channel2");
        channelService.create(testChannel);
        channelService.create(testChannel2);
        channelService.find(testChannel.getId());
        channelService.find(testChannel2.getId());
        channelService.findAll();
    }
}