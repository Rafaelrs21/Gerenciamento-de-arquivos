package br.com.DataPilots.Fileflow.units;

import br.com.DataPilots.Fileflow.entities.Folder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FolderClassTests {
    private Folder folder;

    @BeforeEach
    void setUp() {
        folder = new Folder();
    }

    @Test
    void testDefaultConstructorShouldNotInitializeFields(){
        assertNull(folder.getId());
        assertNull(folder.getFolderId());
        assertNull(folder.getName());
    }

    @Test
    void testConstructorShouldInitializeFields() {
        folder = new Folder(1L,120L, "Minha Pasta");

        assertEquals(folder.getId(), 1L);
        assertEquals(folder.getFolderId(), 120L);
        assertEquals(folder.getName(), "Minha Pasta");
    }

    @Test
    void testSetterMethodsShouldWorkProperly() {
        Folder folder = new Folder();

        folder.setId(2L);
        assertEquals(folder.getId(), 2L);

        folder.setFolderId(120L);
        assertEquals(folder.getFolderId(), 120L);

        folder.setName("Minha Pasta Pode Ser Um Nome");
        assertEquals(folder.getName(), "Minha Pasta Pode Ser Um Nome");
    }

    @Test
    void testSerializeShouldReturnMapWithAllFields() {
        Folder folder = new Folder();
        folder.setId(8888L);
        folder.setFolderId(1554564L);
        folder.setName("Minha Pasta 893829");

        Map<String, Object> data = folder.serialize();

        assertNotNull(data);

        assertEquals(Long.class, data.get("id").getClass());
        assertEquals(Long.class, data.get("folderId").getClass());
        assertEquals(String.class, data.get("name").getClass());

        assertEquals(folder.getId(), (Long) data.get("id"));
        assertEquals(folder.getFolderId(),  (Long) data.get("folderId"));
        assertEquals(folder.getName(), data.get("name"));
    }
}
