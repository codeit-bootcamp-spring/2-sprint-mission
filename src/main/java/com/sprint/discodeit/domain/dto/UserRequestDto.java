package com.sprint.discodeit.domain.dto;

import java.util.UUID;

public record UserRequestDto( String username, String email, String password, UUID ImgUrl) {
}
