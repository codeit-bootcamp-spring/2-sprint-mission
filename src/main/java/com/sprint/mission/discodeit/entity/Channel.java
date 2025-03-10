package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {

    // 필드 선언
    private String channelName;
    private String description;

    // 생선자 선언
    public Channel(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
    }


    // getter
    public String getChannelName() {
        return channelName;
    }

    public String getDescription() {
        return description;
    }


    // 채널 업데이트 메소드 선언
    public void updateChannel(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
        super.update();
        System.out.printf("[ %s ], [ %s ] 로 변경되었습니다.",this.channelName, this.description);
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof Channel channel) {
            return channel.getChannelName().equals(this.getChannelName());
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nChannelName: " + channelName + "\nDescription: " + description +
                "\nUUID: " + this.getId() +
                "\nCreatedAt: " + this.getCreatedAtFormatted() +
                "\nUpdatedAt: " + this.getUpdatedAttFormatted();
    }
}
