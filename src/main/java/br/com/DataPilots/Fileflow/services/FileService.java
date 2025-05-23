package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileVersion;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.exceptions.FileAlreadyExistsException;
import br.com.DataPilots.Fileflow.exceptions.InvalidFileException;
import br.com.DataPilots.Fileflow.repositories.FileVersionRepository;
import jakarta.transaction.Transactional;
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
    private final FileVersionRepository fileVersionRepository;

    public void create(String name, String mimeType, String base64, Long userId, Long folderId) throws InvalidFileException {
        this.checkParams(name,userId, folderId, base64);

        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        long size = decodedBytes.length;

        Timestamp now = new Timestamp(System.currentTimeMillis());
        File file = new File(null, name, mimeType, base64, size, now, userId, folderId);
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

    @Transactional
    public FileVersion updateFile(File updatedFile, Long userId) {
        File existingFile = repository.findById(updatedFile.getId())
            .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));

        if (!existingFile.getUserId().equals(userId)) {
            throw new RuntimeException("Usuário sem permissão");
        }

        int lastVersion = Optional.ofNullable(
            fileVersionRepository.findTopByFileOrderByVersionNumberDesc(existingFile)
        ).map(FileVersion::getVersionNumber).orElse(0);

        FileVersion versionSnapshot = new FileVersion(existingFile, lastVersion + 1);
        fileVersionRepository.save(versionSnapshot);

        existingFile.setName(updatedFile.getName());
        existingFile.setMimeType(updatedFile.getMimeType());
        existingFile.setBase64(updatedFile.getBase64());
        existingFile.setSize((long) Base64.getDecoder().decode(updatedFile.getBase64()).length);
        existingFile.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        repository.save(existingFile);

        return versionSnapshot;
    }


    public boolean isFileNameInUse(String name, Long userId, Long folderId) {
         List<File> fileList = this.repository.findByUserIdAndFolderId(userId, folderId);
         return fileList.stream().anyMatch(file -> file.getName().equals(name));
    }

    public Optional<File> findByNameAndUserIdAndFolderId(String name, Long userId, Long folderId) {
        return this.repository.findByNameAndUserIdAndFolderId(name, userId, folderId);
    }

    public List<File> getFilesByFolder(Long userId, Long folderId) {
        return this.repository.findByUserIdAndFolderId(userId, folderId);
    }

    public List<File> getFilesByUser(Long userId) {
        return this.repository.findByUserId(userId);
    }

    public void delete(File file) {
        this.repository.delete(file);
    }
}