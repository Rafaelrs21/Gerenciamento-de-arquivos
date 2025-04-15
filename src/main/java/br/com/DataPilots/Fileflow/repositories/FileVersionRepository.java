package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileVersionRepository extends JpaRepository<FileVersion, Long> {

    List<FileVersion> findByFileOrderByVersionNumberDesc(File file);

    FileVersion findTopByFileOrderByVersionNumberDesc(File file);

    boolean existsByFileAndVersionNumber(File file, Integer versionNumber);
}