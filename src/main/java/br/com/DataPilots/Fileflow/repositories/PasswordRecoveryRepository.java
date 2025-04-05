package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.PasswordRecovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long> {

    public Optional<PasswordRecovery> findByToken(String token);
}
