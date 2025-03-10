package br.com.DataPilots.Fileflow.units;

import br.com.DataPilots.Fileflow.entities.Folder;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.LongRange;
import net.jqwik.api.footnotes.EnableFootnotes;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@EnableFootnotes
public class FolderPropertiesTest {

    @Property
    void serializeReturnsCorrectMapping(
        @ForAll @LongRange(min = 0) Long id,
        @ForAll @LongRange(min = 0) Long folderId,
        @ForAll @AlphaChars String name
    ) {
        Folder folder = new Folder();
        folder.setId(id);
        folder.setFolderId(folderId);
        folder.setName(name);

        Map<String, Object> serialized = folder.serialize();

        assertThat(serialized).hasSize(3);
        assertThat(serialized.get("id")).isEqualTo(id);
        assertThat(serialized.get("folderId")).isEqualTo(folderId);
        assertThat(serialized.get("name")).isEqualTo(name);
    }
}