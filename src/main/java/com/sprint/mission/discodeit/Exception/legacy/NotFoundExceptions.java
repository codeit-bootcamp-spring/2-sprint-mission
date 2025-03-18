package com.sprint.mission.discodeit.Exception.legacy;

public class NotFoundExceptions {
    public static NotFoundException USER_NOT_FOUND = new NotFoundException(CommonCode.USER_NOT_FOUND);
    public static NotFoundException SERVER_NOT_FOUND = new NotFoundException(CommonCode.SERVER_NOT_FOUND);
    public static NotFoundException CHANNEL_NOF_FOUND = new NotFoundException(CommonCode.CHANNEL_NOT_FOUND);
    public static NotFoundException MESSAGE_NOT_FOUND = new NotFoundException(CommonCode.MESSAGE_NOT_FOUND);
    public static NotFoundException BINARY_CONTENT_NOT_FOUND = new NotFoundException(CommonCode.BINARY_CONTENT_NOT_FOUND);

    public static NotFoundException USER_STATUS_NOT_FOUND = new NotFoundException(CommonCode.USER_STATUS_NOT_FOUND);
    public static NotFoundException READ_STATUS_NOT_FOUND = new NotFoundException(CommonCode.READ_STATUS_NOT_FOUND);

    public static NotFoundException File_NOT_FOUND = new NotFoundException(CommonCode.File_NOT_FOUND);

}

