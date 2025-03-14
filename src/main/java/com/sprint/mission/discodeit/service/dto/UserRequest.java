package com.sprint.mission.discodeit.service.dto;



public record UserRequest(
        String username,
        String email,
        String password,
        byte[] profileImage
){}

