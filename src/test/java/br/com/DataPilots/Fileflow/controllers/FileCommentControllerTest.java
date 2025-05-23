package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.entities.FileComment;
import br.com.DataPilots.Fileflow.services.FileCommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FileCommentControllerTest {
    private FileCommentService fileCommentService;

    private FileCommentController fileCommentController;

    @BeforeEach
    public void setUp() {
        fileCommentService = Mockito.mock(FileCommentService.class);

        fileCommentController = new FileCommentController(fileCommentService);
    }

    @Test
    public void getAllCommentsByFile() {
        Long fileId = 1L;

        List<FileComment> comments = new ArrayList<>();
        comments.add(Mockito.mock(FileComment.class));

        Mockito.when(fileCommentService.findAllByFileId(fileId)).thenReturn(comments);

        var response = fileCommentController.getAllCommentsByFile(fileId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comments, response.getBody());
    }

    @Test
    public void getAllCommentsByUser() {
        Long userId = 1L;

        List<FileComment> comments = new ArrayList<>();
        comments.add(Mockito.mock(FileComment.class));

        Mockito.when(fileCommentService.findAllByUserId(userId)).thenReturn(comments);

        var response = fileCommentController.getAllCommentsByUser(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comments, response.getBody());
    }

    @Test
    public void getCommentById() {
        Long commentId = 1L;
        FileComment comment = Mockito.mock(FileComment.class);

        Mockito.when(fileCommentService.findById(commentId)).thenReturn(Optional.of(comment));

        var response = fileCommentController.getCommentById(commentId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());
    }

    @Test
    public void getCommentById_whenNotFound() {
        Long commentId = 1L;
        FileComment comment = Mockito.mock(FileComment.class);

        Mockito.when(fileCommentService.findById(commentId)).thenReturn(Optional.empty());

        var response = fileCommentController.getCommentById(commentId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void createComment() {
        FileComment comment = Mockito.mock(FileComment.class);

        Mockito.when(fileCommentService.save(comment)).thenReturn(comment);

        var response = fileCommentController.createComment(comment);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());
        Mockito.verify(fileCommentService).save(comment);
    }

    @Test
    public void deleteCommentById() {
        Long commentId = 1L;

        var response = fileCommentController.deleteCommentById(commentId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Mockito.verify(fileCommentService).deleteById(commentId);
    }
}
