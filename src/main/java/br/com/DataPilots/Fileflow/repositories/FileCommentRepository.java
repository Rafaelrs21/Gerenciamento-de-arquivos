package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.FileComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileCommentRepository extends JpaRepository<FileComment, Long> {
}
