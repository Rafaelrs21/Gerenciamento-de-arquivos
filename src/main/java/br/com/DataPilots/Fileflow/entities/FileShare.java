package br.com.DataPilots.Fileflow.entities;

import br.com.DataPilots.Fileflow.utils.ShareTokenGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Table(name="file_shares")
@Entity(name="FileShare")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class FileShare {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant expiresAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    private String shareSeed;
    private boolean isPublic;
    private boolean isTemporary;

    @OneToMany(mappedBy = "fileShare", cascade = CascadeType.ALL)
    private List<FileSharePermission> permissions;

    @PrePersist
    public void prePersist() { 
        createdAt = Instant.now();
        if (shareSeed == null) {
            shareSeed = ShareTokenGenerator.generateSeed();
        }
    }

    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

    public String getPublicToken() {
        return ShareTokenGenerator.generateToken(shareSeed);
    }
}
