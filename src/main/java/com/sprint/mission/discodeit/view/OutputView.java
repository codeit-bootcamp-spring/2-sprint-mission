package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;
import java.util.ArrayList;
import java.util.List;

public class OutputView {
    private OutputView() {
    }

    public static void printServer(List<ChannelDto> channels, UserDto user, ChannelDto currentChannel) {
        List<String> channelUserTexts = new ArrayList<>();
        for (ChannelDto channel : channels) {
            createChannelUserFormat(channels, user, channelUserTexts, channel, currentChannel);
        }

        System.out.printf("""
                        —---------------------------------------------------------
                         코드잇 2기  | %s
                        —---------------------------------------------------------
                        %s
                        —---------------------------------------------------------
                        %n""",
                currentChannel.name(), String.join("\n", channelUserTexts));
    }

    private static void createChannelUserFormat(List<ChannelDto> channels, UserDto owner, List<String> channelUserTexts, ChannelDto channel, ChannelDto currentChannel) {
        List<UserDto> channelUsers = channel.users();
        for (int i = 0; i < channelUsers.size(); i++) {
            UserDto channelUser = channelUsers.get(i);
            String beforeUserName = "   ";
            if (owner.id().equals(channelUser.id())) {// TODO: 2/26/25 equals로 수정
                beforeUserName = " # ";
            }

            String userName = beforeUserName + channelUsers.get(i).name();
            String channelNameFormat = String.format("%-7s", createChannelName(channels, channel, i, currentChannel));
            channelUserTexts.add(
                    String.format("%-10s|                                      |%-6s", channelNameFormat, userName));
        }
    }

    private static String createChannelName(List<ChannelDto> channels, ChannelDto channel, int i,
                                            ChannelDto currentChannel) {
        String channelName = channel.name();
        if (channel.id().equals(currentChannel.id())) {
            channelName = "# " + channelName;
        }

        channelName = validateChannelEmpty(channels, i, channelName);

        return channelName;
    }

    private static String validateChannelEmpty(List<ChannelDto> channels, int i, String defaultName) {
        if (i < 0 || i >= channels.size()) {
            return "";
        }
        return defaultName;
    }
}
