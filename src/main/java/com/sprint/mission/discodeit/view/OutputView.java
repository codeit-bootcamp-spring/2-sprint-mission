package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.application.ChannelDto;
import com.sprint.mission.discodeit.application.UserDto;

public class OutputView {
    private OutputView() {
    }

    public static void printServer(ChannelDto channelDto, UserDto userDto) {
        System.out.printf("""
                —---------------------------------------------------------
                 코드잇 2기  | %s
                —---------------------------------------------------------
                # %s |                                      | # %s
                —---------------------------------------------------------
                %n""", channelDto.name(), channelDto.name(), userDto.name());
    }
}
