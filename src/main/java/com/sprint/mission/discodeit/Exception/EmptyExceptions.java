package com.sprint.mission.discodeit.Exception;

public class EmptyExceptions {
    public static EmptyException EMPTY_SERVER_LIST = new EmptyException(CommonCode.EMPTY_SERVER_LIST);
    public static EmptyException EMPTY_CHANNEL_LIST = new EmptyException(CommonCode.EMPTY_CHANNEL_LIST);
    public static EmptyException EMPTY_MESSAGE_LIST = new EmptyException(CommonCode.EMPTY_MESSAGE_LIST);
    public static EmptyException EMPTY_USER_STATUS_LIST = new EmptyException(CommonCode.EMPTY_USER_STATUS_LIST);
    public static EmptyException EMPTY_READ_STATUS_LIST = new EmptyException(CommonCode.EMPTY_READ_STATUS_LIST);
    public static EmptyException EMPTY_BINARY_CONTENT_LIST = new EmptyException(CommonCode.EMPTY_BINARY_CONTENT_LIST);
}
