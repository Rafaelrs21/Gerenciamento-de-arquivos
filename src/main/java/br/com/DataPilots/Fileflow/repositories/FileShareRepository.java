package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.FileShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileShareRepository extends JpaRepository<FileShare, Long> {
    List<FileShare> findByFileId_Id(Long fileId);
}
