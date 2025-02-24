package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private User sender;
    private String content;
    private Channel channel;
    private boolean isEdited;

    public Message(User sender, String content, Channel channel) {
        super();
        validateSender(sender);
        validateContent(content);
        validateChannel(channel);
        this.sender = sender;
        this.content = content;
        this.channel = channel;
        this.isEdited = false;
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void updateContent(String newContent) {
        validateContent(newContent);
        this.content = newContent;
        this.isEdited = true;
        super.updateUpdatedAt();
    }

    private void validateSender(User sender) {
        if (sender == null) {
            throw new IllegalArgumentException("sender 가 null 일 수는 없다!!!");
        }
        // 유효한 sender 인지 검사하는 방법이 좀 생각하기 어렵다
        // 매개변수로 User들이 저장된 리스트 같은 것을 전달하기에는 너무 지저분해질 것 같다.
        // 어떻게 검사할지는 나중에 작성해야될 듯
    }

    private void validateContent(String content) {
        if (content == null || content.length() == 0) {
            throw new IllegalArgumentException("Content는 null 이거나 길이가 0일 수 없다!!!");
        }
    }

    private void validateChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel 이 null 일 수는 없다!!!");
        }
        // 유효한 channel 인지 검사하는 방법이 좀 생각하기 어렵다
        // 매개변수로 channel들이 저장된 리스트 같은 것을 전달하기에는 너무 지저분해질 것 같다.
        // 어떻게 검사할지는 나중에 작성해야될 듯
    }

    @Override
    public String toString() {
        return "\nMessage\n"
                + "sender: " + sender + "\n"
                + "content: " + content + "\n"
                + "channel: " + channel + "\n"
                + "isEdited: " + isEdited + "\n"
                + super.toString() + "\n";
    }
}
