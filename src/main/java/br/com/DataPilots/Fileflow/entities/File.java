package br.com.DataPilots.Fileflow.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Table(name="files")
@Entity(name="File")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class File {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id);
        data.put("name", name);
        return data;
    }
}