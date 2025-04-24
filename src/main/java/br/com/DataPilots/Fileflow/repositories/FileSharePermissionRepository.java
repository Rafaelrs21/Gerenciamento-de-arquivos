package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.FileSharePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileSharePermissionRepository extends JpaRepository<FileSharePermission, Long> {
} 