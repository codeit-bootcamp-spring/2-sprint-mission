package com.sprint.mission.discodeit.entity;

public enum UserRole {
  USER("ROLE_USER"),
  CHANNEL_MANAGER("ROLE_CHANNEL_MANAGER"),
  ADMIN("ROLE_ADMIN");

  private final String authority;

  UserRole(String authority) {
    this.authority = authority;
  }

  public String getAuthority() {
    return authority;
  }
}
