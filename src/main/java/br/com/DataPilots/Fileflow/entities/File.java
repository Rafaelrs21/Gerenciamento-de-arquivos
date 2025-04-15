package br.com.DataPilots.Fileflow.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;
import lombok.*;

@Table(name="files")
@Entity(name="File")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String mimeType;
    private String base64;
    private Long size;
    private Timestamp createdAt;
    private Long userId;
    private Long folderId;


    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<FileGroupPermission> groupPermissions = new ArrayList<>();

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<FileUserPermission> userPermissions = new ArrayList<>();

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("mimeType", mimeType);
        data.put("base64", base64);
        data.put("size", size);
        data.put("createdAt", createdAt);
        data.put("userId", userId);
        data.put("folderId", folderId);
        return data;
    }
}
