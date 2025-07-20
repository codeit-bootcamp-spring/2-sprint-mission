package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.data.UserDto;
import java.util.ArrayList;
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
  private final Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    authorities.add(new SimpleGrantedAuthority(userDto.role().name()));

    return authorities;
  }

  @Override
  public String getUsername() {
    return userDto.username();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CustomUserDetails that)) return false;
    return userDto.username().equals(that.userDto.username());
  }

  @Override
  public int hashCode() {
    return Objects.hash(userDto.username());
  }
}
