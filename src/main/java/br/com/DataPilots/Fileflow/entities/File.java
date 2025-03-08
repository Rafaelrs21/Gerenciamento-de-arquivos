package br.com.DataPilots.Fileflow.entities;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="files")
@Entity(name="File")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class File {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long folderId;
    private String name;
    private String path;
    private String type;
    private String size;
    private String createdAt;
    private String updatedAt;  

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id);
        data.put("folderId", folderId);
        data.put("name", name);
        data.put("path", path);
        data.put("type", type);
        data.put("size", size);
        data.put("createdAt", createdAt);
        data.put("updatedAt", updatedAt);
        return data;
    }
}
