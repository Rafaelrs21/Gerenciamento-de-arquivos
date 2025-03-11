package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {

}
