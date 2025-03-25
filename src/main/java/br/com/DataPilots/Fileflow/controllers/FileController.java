package br.com.DataPilots.Fileflow.controllers;

import java.util.List;
import java.util.Map;

import br.com.DataPilots.Fileflow.dtos.DefaultResponseDTO;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.services.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.repositories.FileRepository;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;
    private final FileService fileService;

    /*@GetMapping
    public List<Map<String, Object>> getAll() {
        List<File> files = fileRepository.findAll();
        return files.stream().map(File::serialize).toList();
    }*/

    @GetMapping
    public ResponseEntity<List<File>> getAllFile(@AuthenticationPrincipal User user) {
        List<File> all = fileRepository.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
