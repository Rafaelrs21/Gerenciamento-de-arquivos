package br.com.DataPilots.Fileflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.DataPilots.Fileflow.entities.File;

public interface FileRepository extends JpaRepository<File, Long> {

}
