package br.com.DataPilots.Fileflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.DataPilots.Fileflow.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    public boolean existsByUsername(String username);
}