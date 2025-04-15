package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileVersion;
import br.com.DataPilots.Fileflow.repositories.FileVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileVersionService {

    private final FileVersionRepository fileVersionRepository;

    public List<FileVersion> listVersionsByFileId(File file) {
        return fileVersionRepository.findByFileOrderByVersionNumberDesc(file);
    }

    public FileVersion getVersionById(Long versionId) {
        return fileVersionRepository.findById(versionId)
            .orElseThrow(() -> new RuntimeException("Versão não encontrada"));
    }

    public FileVersion getLatestVersion(File file) {
        return fileVersionRepository.findTopByFileOrderByVersionNumberDesc(file);
    }
}
