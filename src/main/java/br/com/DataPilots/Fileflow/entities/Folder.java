package br.com.DataPilots.Fileflow.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;
import lombok.*;

@Table(name="folders")
@Entity(name="Folder")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Folder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id);
        data.put("userId", userId);
        data.put("name", name);
        return data;
    }
}