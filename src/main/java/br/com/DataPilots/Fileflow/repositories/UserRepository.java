package br.com.DataPilots.Fileflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.DataPilots.Fileflow.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    public boolean existsByUsername(String username);
}