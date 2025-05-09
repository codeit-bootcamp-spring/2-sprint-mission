package com.sprint.mission.discodeit.common.constant;

public enum ErrorMessages {
    ERROR_CHANNEL_NOT_FOUND("해당 id의 채널이 존재하지 않습니다"),
    ERROR_MESSAGE_NOT_FOUND("해당 메세지가 없습니다"),
    ERROR_USER_NOT_FOUND("해당 유저가 없습니다"),
    ERROR_USER_NOT_FOUND_BY_EMAIL("Email과 일치하는 유저가 없습니다"),
    ERROR_ACCESS_DENIED_TO_BEAN_STORAGE("빈 저장소는 실행도중 꺼낼 수 없습니다");

    private static final String ERROR = "[ERROR]";
    private final String messageContent;

    ErrorMessages(String messageContent) {
        this.messageContent = ERROR + " " + messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
