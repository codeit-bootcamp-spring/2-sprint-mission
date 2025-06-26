package com.sprint.mission.discodeit.security.entity;

import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final UserDto userDto;
  private final String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_".concat(userDto.role().name())));
  }

  @Override
  public String getUsername() {
    return password;
  }

  @Override
  public String getPassword() {
    return userDto.username();
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof CustomUserDetails that)) {
      return false;
    }
    return userDto.username().equals(that.userDto.username());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(userDto.username());
  }
}
