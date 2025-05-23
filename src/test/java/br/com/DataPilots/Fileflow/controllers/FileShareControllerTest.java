package br.com.DataPilots.Fileflow.controllers;

import br.com.DataPilots.Fileflow.dtos.CreateFileShareDTO;
import br.com.DataPilots.Fileflow.dtos.FileShareResponseDTO;
import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.FileShare;
import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.services.FileShareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileShareControllerTest {

    private FileShareService fileShareService;
    private FileShareController fileShareController;

    @BeforeEach
    public void setUp() {
        fileShareService = Mockito.mock(FileShareService.class);
        fileShareController = new FileShareController(fileShareService);
    }

    @Test
    public void createShare() {
        CreateFileShareDTO dto = Mockito.mock(CreateFileShareDTO.class);
        FileShare share = Mockito.mock(FileShare.class);
        User mockUser = Mockito.mock(User.class);
        Mockito.when(mockUser.getId()).thenReturn(1L);

        User mockOwner = Mockito.mock(User.class);
        Mockito.when(mockOwner.getId()).thenReturn(1L);
        Mockito.when(share.getOwner()).thenReturn(mockOwner);

        Mockito.when(fileShareService.createShare(dto, mockUser.getId())).thenReturn(share);

        var response = fileShareController.createShare(mockUser, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FileShareResponseDTO.fromFileShare(share), response.getBody());
        Mockito.verify(fileShareService).createShare(dto, mockUser.getId());
    }



    @Test
    public void getFileShareById_found() {
        Long shareId = 1L;
        User mockUser = Mockito.mock(User.class);
        FileShare share = Mockito.mock(FileShare.class);
        User mockOwner = Mockito.mock(User.class);

        Mockito.when(mockOwner.getId()).thenReturn(1L);
        Mockito.when(share.getOwner()).thenReturn(mockOwner);
        Mockito.when(mockUser.getId()).thenReturn(1L);
        Mockito.when(fileShareService.findByIdAndUser(shareId, mockUser.getId())).thenReturn(Optional.of(share));

        var response = fileShareController.getFileShareById(mockUser, shareId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FileShareResponseDTO.fromFileShare(share), response.getBody());
    }

    @Test
    public void getFileShareById_notFound() {
        Long shareId = 1L;
        User mockUser = Mockito.mock(User.class);

        Mockito.when(mockUser.getId()).thenReturn(1L);
        Mockito.when(fileShareService.findByIdAndUser(shareId, mockUser.getId())).thenReturn(Optional.empty());

        var response = fileShareController.getFileShareById(mockUser, shareId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void getFileShareByPublicToken_found() {
        String token = "abc123";
        FileShare share = Mockito.mock(FileShare.class);
        User mockOwner = Mockito.mock(User.class);

        Mockito.when(mockOwner.getId()).thenReturn(1L);
        Mockito.when(share.getOwner()).thenReturn(mockOwner);
        Mockito.when(fileShareService.findByPublicToken(token)).thenReturn(Optional.of(share));

        var response = fileShareController.getFileShareByPublicToken(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FileShareResponseDTO.fromFileShare(share), response.getBody());
    }


    @Test
    public void getFileShareByPublicToken_notFound() {
        String token = "abc123";

        Mockito.when(fileShareService.findByPublicToken(token)).thenReturn(Optional.empty());

        var response = fileShareController.getFileShareByPublicToken(token);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getSharesByUser() {
        User mockUser = Mockito.mock(User.class);
        Long userId = 1L;

        Mockito.when(mockUser.getId()).thenReturn(userId);
        List<FileShare> shares = new ArrayList<>();
        FileShare mockShare = Mockito.mock(FileShare.class);
        User mockOwner = Mockito.mock(User.class);
        Mockito.when(mockOwner.getId()).thenReturn(userId);

        Mockito.when(mockShare.getOwner()).thenReturn(mockOwner);
        shares.add(mockShare);
        Mockito.when(fileShareService.findSharesByUserId(userId)).thenReturn(shares);

        var response = fileShareController.getSharesByUser(mockUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(shares.stream()
            .map(FileShareResponseDTO::fromFileShare)
            .collect(Collectors.toList()), response.getBody());
    }

    @Test
    public void getSharesCreatedByUser() {
        User mockUser = Mockito.mock(User.class);
        Long userId = 1L;

        Mockito.when(mockUser.getId()).thenReturn(userId);
        List<FileShare> shares = new ArrayList<>();
        FileShare mockShare = Mockito.mock(FileShare.class);
        User mockOwner = Mockito.mock(User.class);
        Mockito.when(mockOwner.getId()).thenReturn(userId);

        Mockito.when(mockShare.getOwner()).thenReturn(mockOwner);
        shares.add(mockShare);
        Mockito.when(fileShareService.findSharesCreatedByUser(userId)).thenReturn(shares);

        var response = fileShareController.getSharesCreatedByUser(mockUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(shares.stream()
            .map(FileShareResponseDTO::fromFileShare)
            .collect(Collectors.toList()), response.getBody());
    }

    @Test
    public void deleteShare_success() {
        User mockUser = Mockito.mock(User.class);
        Long shareId = 1L;
        Long userId = 1L;

        Mockito.when(mockUser.getId()).thenReturn(userId);
        FileShare mockShare = Mockito.mock(FileShare.class);
        Mockito.when(mockShare.getOwner()).thenReturn(mockUser);
        Mockito.when(mockShare.getFile()).thenReturn(Mockito.mock(File.class));

        Mockito.when(fileShareService.findById(shareId)).thenReturn(Optional.of(mockShare));

        var response = fileShareController.deleteShare(mockUser, shareId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Mockito.verify(fileShareService).deleteShare(shareId, userId);
    }
}