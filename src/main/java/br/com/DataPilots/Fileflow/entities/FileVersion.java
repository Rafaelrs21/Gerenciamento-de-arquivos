package br.com.DataPilots.Fileflow.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "file_versions")
@Data
@NoArgsConstructor
public class FileVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer versionNumber;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private File file;


    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("fileId", file != null ? file.getId() : null);
        data.put("versionNumber", versionNumber);
        data.put("comment", comment);
        return data;
    }
}


