package br.com.DataPilots.Fileflow.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
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

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<FileGroupPermission> groupPermissions = new ArrayList<>();

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<FileUserPermission> userPermissions = new ArrayList<>();

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", id);
        data.put("name", name);
        return data;
    }
}
