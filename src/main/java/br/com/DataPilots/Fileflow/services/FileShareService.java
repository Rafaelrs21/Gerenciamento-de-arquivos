package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.FileShare;
import br.com.DataPilots.Fileflow.repositories.FileShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileShareService {
    private final FileShareRepository fileShareRepository;

    public Optional<FileShare> findById(Long fileId) {
        return fileShareRepository.findById(fileId);
    }

    public FileShare save(FileShare fileShare) {
        return fileShareRepository.save(fileShare);
    }

    public List<FileShare> findByFileId(Long fileId) {
        return fileShareRepository.findByFileId_Id(fileId);
    }
}
