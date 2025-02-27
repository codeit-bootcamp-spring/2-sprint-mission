package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private String message;
    private final User user;
    private final Channel channel;

    public Message(String message, User user, Channel channel) {
        super();
        this.message = message;
        this.user = user;
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public User getUser(){
        return user;
    }

    public Channel getChannel(){
        return channel;
    }

    public Message updateMessage(String message) {
        if(this.message.equals(message)){
            System.out.println("같은 내용으로 변경할 수 없습니다.");
            return this;
        }
        this.message = message;
        update();

        return this;
    }

    @Override
    public String toString() {
        return "Message{'" + message +"', creater= "+user.getUsername()+ ", channel= "+channel.getChannelname()+", ID= " + getId() +", createdtime= "+getCreatedAt()+", updatedtime= "+getUpdatedAt()+"}";
    }

    public String toString(boolean bool){
        if(bool){
            return "Message '"+message+"' 은 삭제되지 않았습니다.";
        }
        return "Message '"+message+"' 은 삭제되었습니다.";
    }
}
