package com.sprint.mission.discodeit.Exception;

public class CommonExceptions {
    public static CommonException USER_NOT_FOUND = new CommonException(CommonCode.USER_NOT_FOUND);
    public static CommonException SERVER_NOT_FOUND = new CommonException(CommonCode.SERVER_NOT_FOUND);
    public static CommonException CHANNEL_NOF_FOUND = new CommonException(CommonCode.CHANNEL_NOT_FOUND);
    public static CommonException MESSAGE_NOT_FOUND = new CommonException(CommonCode.MESSAGE_NOT_FOUND);
    public static CommonException BINARY_CONTENT_NOT_FOUND = new CommonException(CommonCode.BINARY_CONTENT_NOT_FOUND);

    public static CommonException USER_STATUS_NOT_FOUND = new CommonException(CommonCode.USER_STATUS_NOT_FOUND);
    public static CommonException READ_STATUS_NOT_FOUND = new CommonException(CommonCode.READ_STATUS_NOT_FOUND);

    public static CommonException EMPTY_USER_LIST = new CommonException(CommonCode.EMPTY_USER_LIST);
    public static CommonException EMPTY_SERVER_LIST = new CommonException(CommonCode.EMPTY_SERVER_LIST);
    public static CommonException EMPTY_CHANNEL_LIST = new CommonException(CommonCode.EMPTY_CHANNEL_LIST);
    public static CommonException EMPTY_MESSAGE_LIST = new CommonException(CommonCode.EMPTY_MESSAGE_LIST);
    public static CommonException EMPTY_USER_STATUS_LIST = new CommonException(CommonCode.EMPTY_USER_STATUS_LIST);
    public static CommonException EMPTY_READ_STATUS_LIST = new CommonException(CommonCode.EMPTY_READ_STATUS_LIST);
    public static CommonException EMPTY_BINARY_CONTENT_LIST = new CommonException(CommonCode.EMPTY_BINARY_CONTENT_LIST);

    public static CommonException DUPLICATE_USER = new CommonException(CommonCode.DUPLICATE_USER);
    public static CommonException DUPLICATE_USER_STATUS = new CommonException(CommonCode.DUPLICATE_USER_STATUS);
    public static CommonException DUPLICATE_READ_STATUS = new CommonException(CommonCode.DUPLICATE_READ_STATUS);

    public static CommonException INVALID_PASSWORD = new CommonException(CommonCode.INVALID_PASSWORD);

    public static CommonException File_NOT_FOUND = new CommonException(CommonCode.File_NOT_FOUND);

}

