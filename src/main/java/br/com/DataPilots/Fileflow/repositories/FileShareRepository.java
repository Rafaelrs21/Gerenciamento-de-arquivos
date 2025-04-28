package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.FileShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileShareRepository extends JpaRepository<FileShare, Long> {
    List<FileShare> findByFileId_Id(Long fileId);
    Optional<FileShare> findByShareSeed(String seed);

    @Query("SELECT DISTINCT fs FROM FileShare fs " +
           "JOIN fs.permissions p " +
           "WHERE p.user.id = :userId")
    List<FileShare> findSharesByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT fs FROM FileShare fs " +
           "WHERE fs.owner.id = :userId")
    List<FileShare> findSharesCreatedByUser(@Param("userId") Long userId);

    @Query("SELECT fs FROM FileShare fs " +
           "WHERE fs.publico = true " +
           "AND (fs.expiresAt IS NULL OR fs.expiresAt > CURRENT_TIMESTAMP)")
    List<FileShare> findAllPublicAndNotExpired();
}
