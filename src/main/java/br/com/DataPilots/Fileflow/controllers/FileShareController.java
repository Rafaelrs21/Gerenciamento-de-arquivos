package br.com.DataPilots.Fileflow.controllers;



import br.com.DataPilots.Fileflow.entities.FileShare;
import br.com.DataPilots.Fileflow.services.FileShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/share")
@RequiredArgsConstructor
public class FileShareController {
    private final FileShareService fileShareService;

    @GetMapping("/share/{shareId}")
    public ResponseEntity<FileShare> getFileShareById(@PathVariable Long shareId) {
        Optional<FileShare> file = fileShareService.findById(shareId);
        return file.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
