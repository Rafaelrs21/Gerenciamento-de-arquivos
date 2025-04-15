package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileVersion;
import br.com.DataPilots.Fileflow.repositories.FileRepository;
import br.com.DataPilots.Fileflow.services.FileVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("file-versions")
@RequiredArgsConstructor
public class FileVersionController {

    private final FileVersionService fileVersionService;
    private final FileRepository fileRepository;

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<FileVersion>> getAllVersionsByFileId(@PathVariable Long fileId) {
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));

        List<FileVersion> versions = fileVersionService.listVersionsByFileId(file);
        return ResponseEntity.ok(versions);
    }

    @GetMapping("/{versionId}")
    public ResponseEntity<FileVersion> getVersionById(@PathVariable Long versionId) {
        FileVersion version = fileVersionService.getVersionById(versionId);
        return ResponseEntity.ok(version);
    }

    @GetMapping("/file/{fileId}/latest")
    public ResponseEntity<FileVersion> getLatestVersion(@PathVariable Long fileId) {
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));

        FileVersion latest = fileVersionService.getLatestVersion(file);
        return ResponseEntity.ok(latest);
    }
}