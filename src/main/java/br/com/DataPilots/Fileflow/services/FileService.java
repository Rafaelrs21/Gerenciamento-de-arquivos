package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    @Autowired
    private FileRepository repository;

    
}