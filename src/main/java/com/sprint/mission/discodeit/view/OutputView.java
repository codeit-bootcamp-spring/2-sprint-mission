package com.sprint.mission.discodeit.view;

import com.ibm.icu.text.Transliterator;
import com.sprint.mission.discodeit.application.MessageDto;
import com.sprint.mission.discodeit.application.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.application.userdto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class OutputView {
    private static final int CHANNEL_FORMAT_SPECIFIER = 10;
    private static final int MESSAGE_FORMAT_SPECIFIER = 38;
    private static final int USER_FORMAT_SPECIFIER = 9;

    private OutputView() {
    }

    public static void printHello() {
        System.out.println("안녕하세요 코드잇2기 서버입니다.");
    }

    public static void printOtherChannels(List<ChannelResponseDto> channels) {
        System.out.println("# 이동할 채널을 선택해주세요");
        int count = 1;
        for (ChannelResponseDto channel : channels) {
            System.out.println("- " + channel.name() + " : " + count++ + "번");
        }
    }

    public static void printServer(List<ChannelResponseDto> channels, UserDto loginUser, List<MessageDto> messages,
                                   ChannelResponseDto currentChannel) {
        String title = String.format(" 코드잇 2기  | %s", currentChannel.name());
        String content = formatContent(channels, loginUser, messages, currentChannel);

        System.out.printf("""
                —---------------------------------------------------------
                %s
                —---------------------------------------------------------
                %s
                —---------------------------------------------------------
                %n""", title, content);
    }

    private static String formatContent(List<ChannelResponseDto> channels, UserDto loginUser,
                                        List<MessageDto> currentChannelMessages,
                                        ChannelResponseDto currentChannel) {

        List<String> formattedChannels = formatChannels(channels, currentChannel);
        List<String> formattedMessages = formatMessages(currentChannelMessages);
        List<String> formattedUsers = formatUsers(loginUser, currentChannel.usersDto().users());

        addPadding(formattedChannels, formattedMessages, formattedUsers);

        List<String> formattedContents = new ArrayList<>();
        for (int i = 0; i < formattedChannels.size(); i++) {
            String row = String.join("|", formattedChannels.get(i), formattedMessages.get(i), formattedUsers.get(i));
            formattedContents.add(row);
        }

        return String.join("\n", formattedContents);
    }

    private static void addPadding(List<String> formattedChannels, List<String> formattedMessages,
                                   List<String> formattedUsers) {
        int channelSize = formattedChannels.size();
        int messageSize = formattedMessages.size();
        int usersSize = formattedUsers.size();

        int maxSize = Math.max(channelSize, Math.max(messageSize, usersSize));
        addEachPadding(channelSize, maxSize, formattedChannels, CHANNEL_FORMAT_SPECIFIER);
        addEachPadding(messageSize, maxSize, formattedMessages, MESSAGE_FORMAT_SPECIFIER);
        addEachPadding(usersSize, maxSize, formattedUsers, USER_FORMAT_SPECIFIER);
    }

    private static void addEachPadding(int usersSize, int max, List<String> formattedUsers, int count) {
        for (int i = usersSize; i < max; i++) {
            formattedUsers.add(" ".repeat(count));
        }
    }

    private static List<String> formatChannels(List<ChannelResponseDto> channels, ChannelResponseDto currentChannel) {
        List<String> formattedChannels = new ArrayList<>();

        for (ChannelResponseDto channel : channels) {
            String channelName = "  " + channel.name();
            if (channel.id().equals(currentChannel.id())) {
                channelName = "# " + channelName.trim();
            }
            formattedChannels.add(String.format("%-10s", channelName));
        }

        return formattedChannels;
    }

    private static List<String> formatMessages(List<MessageDto> messages) {
        List<String> formattedMessages = new ArrayList<>();
        Transliterator transliterator = Transliterator.getInstance("Hangul-Latin");

        for (MessageDto message : messages) {
            String name = message.user().name();
            String englishName = transliterator.transliterate(name).toUpperCase();
            formattedMessages.add(String.format(" %-36s ", englishName + ": " + message.context()));
        }

        return formattedMessages;
    }

    private static List<String> formatUsers(UserDto loginUser, List<UserDto> channelUsers) {
        List<String> formattedUsers = new ArrayList<>();

        for (UserDto user : channelUsers) {
            String userName = "   " + user.name();
            if (user.id().equals(loginUser.id())) {
                userName = " # " + userName.trim();
            }

            formattedUsers.add(String.format("%-6s", userName));
        }

        return formattedUsers;
    }
}
