package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.FileComment;
import br.com.DataPilots.Fileflow.repositories.FileCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileCommentService {
    private final FileCommentRepository fileCommentRepository;

    public List<FileComment> findAllByFileId(Long fileId) {
        return fileCommentRepository.findByFileId_Id(fileId);
    }

    public List<FileComment> findAllByUserId(Long userId) {
        return fileCommentRepository.findByUserId_Id(userId);
    }

    public Optional<FileComment> findById(Long id) {
        return fileCommentRepository.findById(id);
    }

    public FileComment save(FileComment fileComment) {
        return fileCommentRepository.save(fileComment);
    }

    public void deleteById(Long id) {
        fileCommentRepository.deleteById(id);
    }
}
