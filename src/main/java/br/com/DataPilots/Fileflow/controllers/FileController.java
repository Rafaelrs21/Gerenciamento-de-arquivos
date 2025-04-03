package br.com.DataPilots.Fileflow.controllers;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<File> files = fileRepository.findAll();
        return files.stream().map(File::serialize).toList();
    }
}
