package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByName(String name);

    boolean existsByName(String name);

    List<File> findByUserId(Long userId);
}

