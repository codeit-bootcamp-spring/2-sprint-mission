package com.sprint.mission.discodeit.command;

public class Command {
    private Command() {
    }

    public static final String CHANNEL_CREATION = "1";
    public static final String USER_JOIN = "2";
    public static final String CHANNEL_NAME_UPDATE = "3";
    public static final String MESSAGE_SEND = "4";
    public static final String CHANNEL_CHANGE = "5";
    public static final String EXIT = "6";
}
