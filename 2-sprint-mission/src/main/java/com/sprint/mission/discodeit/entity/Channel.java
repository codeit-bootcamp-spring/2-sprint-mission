package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends BaseEntity {
    private String channelname;
    private final List<User> users = new ArrayList<>();

    public Channel(String channel, User creater) {
        super();
        this.channelname = channel;
        users.add(creater);
    }
    public String getChannelname() {
        return channelname;
    }

    public Channel updateChannel(String channel) {
        if(this.channelname.equals(channel)){
            System.out.println("같은 이름으로 변경할 수 없습니다.");
            return this;
        }
        this.channelname = channel;
        update();
        return this;
    }

    public Channel addUser(User user){
        users.add(user);
        return this;
    }

    @Override
    public String toString() {
        return "Channel{name= '" + channelname +", creater= "+users.getFirst().getUsername()+ "', ID= " + getId() +", createdtime= "+getCreatedAt()+", updatedtime= "+getUpdatedAt()+"}";
    }

    public String toString(boolean bool){
        if(bool){
            return "Channel "+channelname+"은 삭제되지 않았습니다.";
        }
        return "Channel "+channelname+"은 삭제되었습니다.";
    }
}
