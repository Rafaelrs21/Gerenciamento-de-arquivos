package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.dtos.CreateFileShareDTO;
import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileShare;
import br.com.DataPilots.Fileflow.entities.FileSharePermission;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.InvalidFileException;
import br.com.DataPilots.Fileflow.exceptions.InvalidShareException;
import br.com.DataPilots.Fileflow.exceptions.InvalidUserException;
import br.com.DataPilots.Fileflow.exceptions.InvalidUserIdException;
import br.com.DataPilots.Fileflow.exceptions.SharePermissionDeniedException;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.repositories.FileShareRepository;
import br.com.DataPilots.Fileflow.repositories.FileSharePermissionRepository;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import br.com.DataPilots.Fileflow.utils.ShareTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileShareService {
    private final FileShareRepository fileShareRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileSharePermissionRepository fileSharePermissionRepository;

    @Transactional
    public FileShare createShare(CreateFileShareDTO dto) {
        File file = fileRepository.findById(dto.getFileId())
                .orElseThrow(InvalidFileException::new);

        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(InvalidUserIdException::new);

        FileShare share = new FileShare();
        share.setFile(file);
        share.setOwner(owner);
        share.setExpiresAt(dto.getExpiresAt());
        share.setPublic(dto.isPublic());
        share.setTemporary(dto.isTemporary());

        FileShare savedShare = fileShareRepository.save(share);

        if (dto.getUserIds() != null) {
            for (Long userId : dto.getUserIds()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(InvalidUserIdException::new);

                FileSharePermission permission = new FileSharePermission();
                permission.setFileShare(savedShare);
                permission.setUser(user);
                permission.setCanEdit(dto.isCanEdit());
                permission.setCanShare(dto.isCanShare());
                
                fileSharePermissionRepository.save(permission);
            }
        }

        return savedShare;
    }

    public FileShare findById(Long id) {
        return fileShareRepository.findById(id)
                .orElseThrow(InvalidShareException::new);
    }

    public FileShare findByPublicToken(String token) {
        // Extrai a seed do token (primeiros 8 caracteres)
        String seed = token.substring(0, 8);
        
        FileShare share = fileShareRepository.findByShareSeed(seed)
                .orElseThrow(InvalidShareException::new);
        
        if (share.isExpired()) {
            throw new InvalidShareException();
        }
        
        if (!share.isPublic()) {
            throw new SharePermissionDeniedException();
        }
        
        // Valida se o token gerado a partir da seed é igual ao token fornecido
        if (!ShareTokenGenerator.validateToken(seed, token)) {
            throw new InvalidShareException();
        }
        
        return share;
    }

    public FileShare findByIdAndUser(Long id, Long userId) {
        FileShare share = findById(id);
        
        boolean hasPermission = share.getPermissions().stream()
                .anyMatch(permission -> permission.getUser().getId().equals(userId));
                
        if (!hasPermission) {
            throw new SharePermissionDeniedException();
        }
        
        return share;
    }

    public List<FileShare> findSharesByUserId(Long userId) {
        return fileShareRepository.findSharesByUserId(userId);
    }

    public List<FileShare> findSharesCreatedByUser(Long userId) {
        return fileShareRepository.findSharesCreatedByUser(userId);
    }

    @Transactional
    public void deleteShare(Long id) {
        fileShareRepository.deleteById(id);
    }

    public List<FileShare> findByFileId(Long fileId) {
        return fileShareRepository.findByFileId_Id(fileId);
    }
}
