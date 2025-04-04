package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.FileComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileCommentRepository extends JpaRepository<FileComment, Long> {
    List<FileComment> findByFileId_Id(Long fileId);
    List<FileComment> findByUserId_Id(Long userId);
}
