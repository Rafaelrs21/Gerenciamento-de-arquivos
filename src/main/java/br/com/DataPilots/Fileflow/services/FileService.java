package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.AppConsts;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.InvalidPasswordLengthException;
import br.com.DataPilots.Fileflow.exceptions.InvalidUserException;
import br.com.DataPilots.Fileflow.exceptions.UserAlreadyExistsException;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    @Autowired
    private FileRepository repository;

    
}