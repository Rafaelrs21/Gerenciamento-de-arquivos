package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.FileComment;
import br.com.DataPilots.Fileflow.services.FileCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/file-comment")
@RequiredArgsConstructor
public class FileCommentController {
    private final FileCommentService fileCommentService;

    @GetMapping("/file/{fileId}")
    public ResponseEntity<List<FileComment>> getAllCommentsByFile(@PathVariable Long fileId) {
        List<FileComment> comments = fileCommentService.findAllByFileId(fileId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileComment>> getAllCommentsByUser(@PathVariable Long userId) {
        List<FileComment> comments = fileCommentService.findAllByUserId(userId);
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
