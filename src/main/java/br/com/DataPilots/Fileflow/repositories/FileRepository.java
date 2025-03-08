package br.com.DataPilots.Fileflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.DataPilots.Fileflow.entities.Folder;

public interface FileRepository extends JpaRepository<Folder, Long> {
    
}
