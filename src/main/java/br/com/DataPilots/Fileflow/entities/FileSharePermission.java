package br.com.DataPilots.Fileflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name="file_share_permissions")
@Entity(name="FileSharePermission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class FileSharePermission {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_share_id")
    private FileShare fileShare;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean canEdit;
    private boolean canShare;
} 