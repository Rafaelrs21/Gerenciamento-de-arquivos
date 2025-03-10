package br.com.DataPilots.Fileflow.entities;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name="folders")
@Entity(name="Folder")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Folder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long folderId;
    private String name;

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id);
        data.put("folderId", folderId);
        data.put("name", name);
        return data;
    }
}
