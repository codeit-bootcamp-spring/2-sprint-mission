package com.sprint.mission.discodeit.entity;

public class Channel extends MainDomain {
    private String channelName;
    private ChannelType type;


    public Channel(ChannelType type, String channelName) {
        super();
        this.type = type;
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
    
    public ChannelType getType(){
        return type;
    }



    public void updateChannel(String newName) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.channelName)) {
            this.channelName = newName;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            update();
        }
    }

    public void updateChannelType(ChannelType newType) {
        if(type==newType || newType==null){
            return;
        }
        this.type = newType;
    }


    @Override
    public String toString() {
        return "Channel{" +
                "channelID= " + getId() +
                ", channelName=" + channelName  +
                ", type=" + type + '}';
    }
}
