package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.exceptions.FileAlreadyExistsException;
import br.com.DataPilots.Fileflow.exceptions.InvalidFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository repository;

    public void create(String name, String mimeType, String base64, Long userId, Long folderId) throws InvalidFileException {
        this.checkParams(name, base64);

        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        long size = decodedBytes.length;

        Timestamp now = new Timestamp(System.currentTimeMillis());
        File file = new File(null, name, mimeType, base64, size, now, userId, folderId);
        this.repository.save(file);
    }

    private void checkParams(String name, String base64) throws InvalidFileException {
        if (base64 == null || base64.isBlank()) {
            throw new InvalidFileException();
        }

        if (this.isFileNameInUse(name)) {
            throw new FileAlreadyExistsException();
        }
    }

    public boolean isFileNameInUse(String name) {
        return this.repository.existsByName(name);
    }

    public Optional<File> getFileByName(String name) {
        return this.repository.findByName(name);
    }

    public List<File> getFilesByUser(Long userId) {
        return this.repository.findByUserId(userId);
    }

    public void delete(File file) {
        this.repository.delete(file);
    }
}