package com.sprint.mission.discodeit.security.userDetails;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {

  private static final long serialVersionUID = 1L;

  private final UserResult userResult;
  private final String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + userResult.role()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userResult.username();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CustomUserDetails that = (CustomUserDetails) o;

    return userResult.username().equals(that.userResult.username());
  }

  @Override
  public int hashCode() {
    return userResult.username().hashCode();
  }

}
