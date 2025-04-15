package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.Folder;
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
        this.checkParams(name,userId, folderId, base64);

        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        long size = decodedBytes.length;

        Timestamp now = new Timestamp(System.currentTimeMillis());
        File file = File.builder()
                .name(name)
                .mimeType(mimeType)
                .base64(base64)
                .size(size)
                .createdAt(now)
                .userId(userId)
                .folderId(folderId)
                .build();
        this.repository.save(file);
    }

    public String downloadFile(String name, Long userId, Long folderId) throws InvalidFileException {
        Optional<File> fileOptional = findByNameAndUserIdAndFolderId(name,userId,folderId);

        if (fileOptional.isEmpty()) {
            throw new InvalidFileException();
        }

        return fileOptional.get().getBase64();
    }

    private void checkParams(String name,Long userId,Long folderId, String base64) throws InvalidFileException {
        if (base64 == null || base64.isBlank()) {
            throw new InvalidFileException();
        }

        if (this.isFileNameInUse(name,userId,folderId)) {
            throw new FileAlreadyExistsException();
        }
    }

    public boolean isFileNameInUse(String name, Long userId, Long folderId) {
         List<File> fileList = this.repository.findByUserIdAndFolderId(userId, folderId);
         return fileList.stream().anyMatch(file -> file.getName().equals(name));
    }

    public Optional<File> findByNameAndUserIdAndFolderId(String name, Long userId, Long folderId) {
        return this.repository.findByNameAndUserIdAndFolderId(name, userId, folderId);
    }

    public List<File> getFilesByUser(Long userId) {
        return this.repository.findByUserId(userId);
    }

    public void delete(File file) {
        this.repository.delete(file);
    }
}
