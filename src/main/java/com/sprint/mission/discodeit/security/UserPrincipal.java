package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.User;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class UserPrincipal implements UserDetails, Serializable {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getAuthority()));
  }

  @Override
  public String getPassword() {
    return user.getPassword();

  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

}
