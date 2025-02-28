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

    public static void printServer(List<ChannelDto> channels, UserDto loginUser, List<MessageDto> messages,
                                   ChannelDto currentChannel) {
        String title = String.format(" 코드잇 2기  | %s", currentChannel.name());
        String content = formatContent(channels, loginUser, messages, currentChannel);

        System.out.printf("""
                        —---------------------------------------------------------
                        %s
                        —---------------------------------------------------------
                        %s
                        —---------------------------------------------------------
                        %n""",
                title, content);
    }

    private static String formatContent(List<ChannelDto> channels, UserDto loginUser,
                                      List<MessageDto> currentChannelMessages,
                                      ChannelDto currentChannel) {

        List<String> formattedChannels = formatChannels(channels, currentChannel);
        List<String> formattedMessages = formatMessages(currentChannelMessages);
        List<String> formattedUsers = formatUsers(loginUser, currentChannel.users()); // 여기서 지금 id가 다르다,

        int channelSize = formattedChannels.size();
        int messageSize = formattedMessages.size();
        int usersSize = formattedUsers.size();

        int max = Math.max(channelSize, Math.max(messageSize, usersSize));
        for (int i = channelSize; i <= max; i++) {
            formattedChannels.add(" ".repeat(10));
        }

        for (int i = messageSize; i <= max; i++) {
            formattedMessages.add(" ".repeat(38));
        }

        for (int i = usersSize; i <= max; i++) {
            formattedUsers.add(" ".repeat(9));
        }

        List<String> formattedContents = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            String row = String.join("|", formattedChannels.get(i), formattedMessages.get(i), formattedUsers.get(i));
            formattedContents.add(row);
        }

        return String.join("\n", formattedContents);
    }

    // 마지막 ---- 처리
//        channelUserTexts.add(
//                String.format("%-10s| %-36s |%-6s", formattedChannels, formattedMessages, formattedUsers));

    // "%-10s|
    private static List<String> formatChannels(List<ChannelDto> channels, ChannelDto currentChannel) {
        List<String> formattedChannels = new ArrayList<>();

        for (ChannelDto channel : channels) {
            String channelName = "  " + channel.name();
            if (channel.id().equals(currentChannel.id())) {
                channelName = "# " + channelName.trim();
            }
            formattedChannels.add(String.format("%-10s", channelName));
        }

        return formattedChannels;
    }
    // | %-36s |
    private static List<String> formatMessages(List<MessageDto> messages) {
        // 이거 조정 해줘야됨 36자 이상이면 33자 까지 자르고 ... 해줘야함
        List<String> formattedMessages = new ArrayList<>();
        Transliterator transliterator = Transliterator.getInstance("Hangul-Latin");

        for (MessageDto message : messages) {
            String name = message.user().name();
            String englishName = transliterator.transliterate(name).toUpperCase();
            formattedMessages.add(String.format(" %-36s ", englishName + ": " + message.context()));
        }

        return formattedMessages;
    }
    // |%-6s
    private static List<String> formatUsers(UserDto loginUser, List<UserDto> channelUsers) {
        List<String> formattedUsers = new ArrayList<>();

        for (UserDto user : channelUsers) {
            String userName = "   " + user.name(); // 이거 위에 채널이랑 통일 시키기
            if (user.id().equals(loginUser.id())) {
                userName = " # " + userName.trim();
            }

            formattedUsers.add(String.format("%-6s", userName));
        }

        return formattedUsers;
    }
}
