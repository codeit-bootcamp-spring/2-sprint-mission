package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;

public class OutputView {
    private OutputView() {
    }

    public static void printServer(ChannelDto channelDto, UserDto userDto) {
        int totalWidth = 7;
        String channelName = String.format("%-" + totalWidth + "s", channelDto.name());

        System.out.printf("""
                —---------------------------------------------------------
                 코드잇 2기  | %s
                —---------------------------------------------------------
                # %s |                                      | # %s
                —---------------------------------------------------------
                %n""", channelDto.name(), channelName, userDto.name());
    }
}
