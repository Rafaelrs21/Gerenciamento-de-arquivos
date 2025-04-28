package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.dtos.CreateFileShareDTO;
import br.com.DataPilots.Fileflow.dtos.SharePermissionDTO;
import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileShare;
import br.com.DataPilots.Fileflow.entities.FileSharePermission;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.exceptions.*;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.repositories.FileShareRepository;
import br.com.DataPilots.Fileflow.repositories.FileSharePermissionRepository;
import br.com.DataPilots.Fileflow.repositories.UserRepository;
import br.com.DataPilots.Fileflow.utils.ShareTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileShareService {
    private final FileShareRepository fileShareRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileSharePermissionRepository fileSharePermissionRepository;

    @Transactional
    public FileShare createShare(CreateFileShareDTO createFileShareDTO, Long userId) {
        File file = fileRepository.findById(createFileShareDTO.getFileId())
            .orElseThrow(() -> new FileNotFoundException("Arquivo não encontrado"));

        User owner = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!file.getUserId().equals(userId)) {
            throw new UnauthorizedOperationException("Usuário não tem permissão para compartilhar este arquivo");
        }

        FileShare share = new FileShare();
        share.setFile(file);
        share.setOwner(owner);
        share.setPublico(createFileShareDTO.isPublico());
        share.setExpiresAt(createFileShareDTO.getExpiresAt());
        share.setTemporario(createFileShareDTO.isTemporario());

        // Salva o compartilhamento primeiro para ter o ID
        share = fileShareRepository.save(share);

        // Adiciona as permissões se não for público ou mesmo se for público mas tiver permissões específicas
        if (createFileShareDTO.getPermissions() != null && !createFileShareDTO.getPermissions().isEmpty()) {
            for (SharePermissionDTO permissionDTO : createFileShareDTO.getPermissions()) {
                User user = userRepository.findById(permissionDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

                FileSharePermission permission = new FileSharePermission();
                permission.setFileShare(share);
                permission.setUser(user);
                permission.setCanEdit(permissionDTO.isCanEdit());
                permission.setCanShare(permissionDTO.isCanShare());
                fileSharePermissionRepository.save(permission);
                share.getPermissions().add(permission);
            }
        }

        // Recarrega o compartilhamento para garantir que todas as permissões estejam presentes
        return fileShareRepository.findById(share.getId())
            .orElseThrow(() -> new InvalidShareException("Erro ao criar compartilhamento"));
    }

    public Optional<FileShare> findById(Long id) {
        return fileShareRepository.findById(id);
    }

    public Optional<FileShare> findByPublicToken(String token) {
        try {
            System.out.println("Buscando share com token: " + token);
            List<FileShare> shares = fileShareRepository.findAllPublicAndNotExpired();
            System.out.println("Shares públicos encontrados: " + shares.size());
            
            for (FileShare share : shares) {
                if (!share.isPublico()) {
                    continue;
                }
                
                String generatedToken = share.getPublicToken();
                System.out.println("Share ID: " + share.getId() + 
                                 ", Seed: " + share.getShareSeed() + 
                                 ", Token gerado: " + generatedToken + 
                                 ", Token recebido: " + token +
                                 ", É público: " + share.isPublico());
                
                if (token.equals(generatedToken)) {
                    return Optional.of(share);
                }
            }
            
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("Erro ao buscar share: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<FileShare> findByIdAndUser(Long id, Long userId) {
        Optional<FileShare> shareOpt = findById(id);
        
        if (shareOpt.isEmpty()) {
            return Optional.empty();
        }
        
        FileShare share = shareOpt.get();
        
        if (share.getOwner().getId().equals(userId) || 
            share.getFile().getUserId().equals(userId)) {
            return Optional.of(share);
        }
        
        if (share.isPublico() && !share.isExpired()) {
            return Optional.of(share);
        }
        
        boolean hasPermission = share.getPermissions().stream()
                .anyMatch(permission -> permission.getUser().getId().equals(userId));
                
        if (!hasPermission) {
            return Optional.empty();
        }
        
        return Optional.of(share);
    }

    public List<FileShare> findSharesByUserId(Long userId) {
        return fileShareRepository.findSharesByUserId(userId);
    }

    public List<FileShare> findSharesCreatedByUser(Long userId) {
        return fileShareRepository.findSharesCreatedByUser(userId);
    }

    @Transactional
    public void deleteShare(Long id, Long userId) {
        Optional<FileShare> shareOpt = findById(id);
        if (shareOpt.isEmpty()) {
            throw new InvalidShareException();
        }

        FileShare share = shareOpt.get();
        
        if (!share.getOwner().getId().equals(userId) && 
            !share.getFile().getUserId().equals(userId)) {
            throw new SharePermissionDeniedException();
        }

        fileShareRepository.deleteById(id);
    }

    public List<FileShare> findByFileId(Long fileId) {
        return fileShareRepository.findByFileId_Id(fileId);
    }

    public boolean hasSharePermission(Long shareId, Long userId) {
        FileShare share = fileShareRepository.findById(shareId)
            .orElseThrow(() -> new InvalidShareException("Compartilhamento não encontrado"));

        if (share.isPublico() && !share.isExpired()) {
            return true;
        }

        if (share.getOwner().getId().equals(userId)) {
            return true;
        }

        return share.getPermissions().stream()
            .anyMatch(permission -> permission.getUser().getId().equals(userId));
    }
}
