package br.com.DataPilots.Fileflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "file_user_permissions")
@Data
@NoArgsConstructor
public class FileUserPermission {
    @EmbeddedId
    private FileUserPermissionPK id;

    @ManyToOne
    @MapsId("fileId")
    @JoinColumn(nullable = false)
    private File file;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission permission;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public FileUserPermission(File file, User user, Permission permission) {
        this.id = new FileUserPermissionPK(file.getId(), user.getId());
        this.file = file;
        this.user = user;
        this.permission = permission;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class FileUserPermissionPK implements Serializable {
        @Column(name = "file_id")
        private Long fileId;

        @Column(name = "user_id")
        private Long userId;
    }
}
