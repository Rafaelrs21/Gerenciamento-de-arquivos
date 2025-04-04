package br.com.DataPilots.Fileflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "group_users")
@Data
@NoArgsConstructor
public class GroupUser {
    @EmbeddedId
    private GroupUserPK id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(nullable = false)
    private Group group;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "joined_at")
    private Instant joinedAt;

    public GroupUser(Group group, User user) {
        this.id = new GroupUserPK(group.getId(), user.getId());
        this.group = group;
        this.user = user;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class GroupUserPK implements Serializable {
        @Column(name = "group_id")
        private Long groupId;

        @Column(name = "user_id")
        private Long userId;
    }
}