package com.sprint.mission.discodeit.view;

import com.ibm.icu.text.Transliterator;
import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.UserDto;
import java.util.ArrayList;
import java.util.List;

public class OutputView {
    private OutputView() {
    }

    public static void printServer(List<ChannelDto> channels, UserDto user, List<MessageDto> messages,
                                   ChannelDto currentChannel) {
        List<String> channelUserTexts = new ArrayList<>();
        for (ChannelDto channel : channels) {
            createChannelUserFormat(channels, user, messages, channelUserTexts, channel, currentChannel);
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

    private static void createChannelUserFormat(List<ChannelDto> channels, UserDto owner, List<MessageDto> messages,
                                                List<String> channelUserTexts, ChannelDto channel,
                                                ChannelDto currentChannel) {
        List<UserDto> channelUsers = channel.users(); // 채널 유저를 기준으로 작성됩니다.
        for (int i = 0; i < channelUsers.size(); i++) {
            UserDto channelUser = channelUsers.get(i);

            String userNameFormat = createUserName(owner, channelUsers, i, channelUser);
            String channelNameFormat = createChannelName(channels, channel, i, currentChannel);
            String messageContext = createMessageContext(messages, channel, i);

            channelUserTexts.add(
                    String.format("%-10s| %-36s |%-6s", channelNameFormat, messageContext, userNameFormat));
        }
    }

    private static String createUserName(UserDto owner, List<UserDto> channelUsers, int i, UserDto channelUser) {
        if (i < 0 || i >= channelUsers.size()) {
            return "";
        }

        String beforeUserName = "   ";
        if (owner.id().equals(channelUser.id())) {// TODO: 2/26/25 equals로 수정
            beforeUserName = " # ";
        }

        return beforeUserName + channelUsers.get(i).name();
    }

    private static String createChannelName(List<ChannelDto> channels, ChannelDto channel, int i, ChannelDto currentChannel) {
        if (i < 0 || i >= channels.size()) {
            return "";
        }

        String channelName = "  " + channel.name();
        if (channel.id().equals(currentChannel.id())) {
            channelName = "# " + channelName.trim();
        }

        return channelName;
    }

    private static String createMessageContext(List<MessageDto> messages, ChannelDto channel, int i) {
        if (i < 0 || i >= messages.size()) {
            return "";
        }

        String name = channel.users()
                .stream()
                .filter(user -> user.id().equals(messages.get(i).userId()))
                .findFirst()
                .map(UserDto::name)
                .orElse("Unknown User");

        Transliterator transliterator = Transliterator.getInstance("Hangul-Latin");
        String englishName = transliterator.transliterate(name).toUpperCase();

        return englishName + ": " + messages.get(i).context();
    }
}
