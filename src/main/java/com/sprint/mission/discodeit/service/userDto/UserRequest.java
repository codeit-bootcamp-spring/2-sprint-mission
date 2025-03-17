package com.sprint.mission.discodeit.service.userDto;



public record UserRequest(
        String username,
        String email,
        String password,
        byte[] profileImage
){}

