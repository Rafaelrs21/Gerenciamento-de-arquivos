package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.FileComment;
import br.com.DataPilots.Fileflow.services.FileCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/file-comments")
@RequiredArgsConstructor
public class FileCommentController {
    private final FileCommentService fileCommentService;
    @GetMapping
    public ResponseEntity<List<FileComment>> getAllComments() {
        List<FileComment> comments = fileCommentService.findAll();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileComment> getCommentById(@PathVariable Long id) {
        Optional<FileComment> comment = fileCommentService.findById(id);
        return comment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FileComment> createComment(@RequestBody FileComment fileComment) {
        FileComment savedComment = fileCommentService.save(fileComment);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id) {
        fileCommentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
