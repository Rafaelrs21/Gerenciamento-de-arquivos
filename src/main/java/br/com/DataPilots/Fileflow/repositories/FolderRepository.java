package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

}
