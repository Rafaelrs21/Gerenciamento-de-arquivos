package br.com.DataPilots.Fileflow.services;

import br.com.DataPilots.Fileflow.entities.FileComment;
import br.com.DataPilots.Fileflow.repositories.FileCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileCommentServiceTest {
    private FileCommentRepository fileCommentRepository;

    private FileCommentService fileCommentService;

    @BeforeEach
    public void setUp() {
        fileCommentRepository  = Mockito.mock(FileCommentRepository.class);

        fileCommentService = new FileCommentService(fileCommentRepository);
    }

    @Test
    public void findAllByFileId() {
        Long fileId = 1L;

        List<FileComment> comments = new ArrayList<>();
        comments.add(Mockito.mock(FileComment.class));

        Mockito.when(fileCommentRepository.findByFileId_Id(fileId)).thenReturn(comments);

        List<FileComment> fileComments = fileCommentService.findAllByFileId(fileId);

        assertEquals(comments, fileComments);
    }

    @Test
    public void findAllByUserId() {
        Long userId = 1L;

        List<FileComment> comments = new ArrayList<>();
        comments.add(Mockito.mock(FileComment.class));

        Mockito.when(fileCommentRepository.findByUserId_Id(userId)).thenReturn(comments);

        List<FileComment> fileComments = fileCommentService.findAllByUserId(userId);

        assertEquals(comments, fileComments);
    }

    @Test
    public void findById() {
        Long fileId = 1L;

        FileComment comment = Mockito.mock(FileComment.class);

        Mockito.when(fileCommentRepository.findById(fileId)).thenReturn(Optional.of(comment));

        var result = fileCommentService.findById(fileId);

        assertEquals(comment, result.get());
    }

    @Test
    public void save() {
        FileComment comment = Mockito.mock(FileComment.class);

        fileCommentService.save(comment);

        Mockito.verify(fileCommentRepository, Mockito.times(1)).save(comment);
    }

    @Test
    public void deleteById() {
        Long fileId = 1L;

        fileCommentService.deleteById(fileId);

        Mockito.verify(fileCommentRepository, Mockito.times(1)).deleteById(fileId);
    }
}
