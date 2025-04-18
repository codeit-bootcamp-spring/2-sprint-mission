package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private ChannelType type;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 500)
  private String description;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReadStatus> readStatus;

  public Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
    }
    if (newDescription != null && !newDescription.equals(this.description)) {
      this.description = newDescription;
    }
  }
}
