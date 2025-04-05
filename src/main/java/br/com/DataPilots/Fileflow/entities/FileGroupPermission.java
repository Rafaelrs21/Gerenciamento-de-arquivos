package br.com.DataPilots.Fileflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "file_group_permissions")
@Data
@NoArgsConstructor
public class FileGroupPermission {
    @EmbeddedId
    private FileGroupPermissionPK id;

    @ManyToOne
    @MapsId("fileId")
    @JoinColumn(nullable = false)
    private File file;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission permission;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public FileGroupPermission(File file, Group group, Permission permission) {
        this.id = new FileGroupPermissionPK(file.getId(), group.getId());
        this.file = file;
        this.group = group;
        this.permission = permission;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class FileGroupPermissionPK implements Serializable {
        @Column(name = "file_id")
        private Long fileId;

        @Column(name = "group_id")
        private Long groupId;
    }
}
