package br.com.DataPilots.Fileflow.services;


import br.com.DataPilots.Fileflow.repositories.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository repository;
    
}