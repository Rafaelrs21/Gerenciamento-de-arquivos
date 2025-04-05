package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.FileGroupPermission;
import br.com.DataPilots.Fileflow.entities.FileGroupPermission.FileGroupPermissionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileGroupPermissionRepository extends JpaRepository<FileGroupPermission, FileGroupPermissionPK> {
}
