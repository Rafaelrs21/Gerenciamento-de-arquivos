package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.AppConsts;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.InvalidPasswordLengthException;
import br.com.DataPilots.Fileflow.exceptions.InvalidUserException;
import br.com.DataPilots.Fileflow.exceptions.UserAlreadyExistsException;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User find(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public User create(String username, String password) throws InvalidUserException {
        this.checkParams(username, password);

        String encodedPassword = this.encodePassword(password);
        User user = new User(username, encodedPassword);

        this.repository.save(user);
        return user;
    }

    private void checkParams(String username, String password) throws InvalidUserException {
        if (password.length() < AppConsts.MIN_PASSWORD_LENGTH)
            throw new InvalidPasswordLengthException();

        if (this.isUsernameInUse(username))
            throw new UserAlreadyExistsException();
    }

    public boolean isUsernameInUse(String username) {
        return this.repository.existsByUsername(username);
    }

    private String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    public void delete(User user) {
        this.repository.delete(user);
    }
}