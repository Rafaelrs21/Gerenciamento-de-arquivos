package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByNameAndUserIdAndFolderId(String name, Long userId, Long folderId);

    List<File> findByUserId(Long userId);

    List<File> findByUserIdAndFolderId(Long userId, Long folderId);

}