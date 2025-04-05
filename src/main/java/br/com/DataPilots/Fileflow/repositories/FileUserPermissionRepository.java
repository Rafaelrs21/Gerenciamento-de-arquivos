package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.FileUserPermission;
import br.com.DataPilots.Fileflow.entities.FileUserPermission.FileUserPermissionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUserPermissionRepository extends JpaRepository<FileUserPermission, FileUserPermissionPK> {
}
