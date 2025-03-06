package com.sprint.mission.discodeit.entity;

public class Channel extends MainDomain {
    private String channelName;
    private ChannelType type;
    private String description;


    public Channel(ChannelType type, String channelName,  String description) {
        super();
        this.type = type;
        this.channelName = channelName;
        this.description = description;
    }

    public String getChannelName() {
        return channelName;
    }
    
    public ChannelType getType(){
        return type;
    }

    public String getDescription() {
        return description;
    }


    public void updateChannel(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.channelName)) {
            this.channelName = newName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            update();
        }
    }


}
