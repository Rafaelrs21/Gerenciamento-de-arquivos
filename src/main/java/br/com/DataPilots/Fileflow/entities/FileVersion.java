package br.com.DataPilots.Fileflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "file_versions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class FileVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer versionNumber;

    private String name;
    private String mimeType;
    private String base64;
    private Long size;
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    public FileVersion(File file, Integer versionNumber) {
        this.file = file;
        this.versionNumber = versionNumber;
        this.name = file.getName();
        this.mimeType = file.getMimeType();
        this.base64 = file.getBase64();
        this.size = file.getSize();
        this.createdAt = file.getCreatedAt();
    }
}
