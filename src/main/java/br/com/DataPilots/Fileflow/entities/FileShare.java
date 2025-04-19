package br.com.DataPilots.Fileflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File fileId;

    @PrePersist
    public void prePersist() { createdAt = Instant.now(); }

    public Instant expirationDate() { return createdAt; }
}
