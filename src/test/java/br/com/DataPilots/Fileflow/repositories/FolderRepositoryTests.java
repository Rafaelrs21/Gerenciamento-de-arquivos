package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.Folder;
import br.com.DataPilots.Fileflow.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class FolderRepositoryTests {
    @Autowired
    private FolderRepository repository;

    private Long existingId = 1L;
    private Long nonExistingId = 1000L;
    private Long countTotalProducts = 0L;

    @BeforeEach
    void setUp() throws Exception {
        repository.deleteAll();
        countTotalProducts = repository.count();
        if (countTotalProducts != 0) {
            throw new IllegalStateException("DB não está vazio.");
        }

        Folder folder = Factory.createFolder();
        repository.save(folder);

        existingId = folder.getId();
        nonExistingId = 1000L;

        countTotalProducts = repository.count();
    }

    @Test
    public void findByIdShouldReturnOptionalNonEmptyWhenIdExists(){
        Optional<Folder> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnOptionalEmptyWhenIdNonExists(){
        Optional<Folder> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        Folder folder = Factory.createFolder();
        folder = repository.save(folder);

        Assertions.assertNotNull(folder.getId());
        Assertions.assertEquals(countTotalProducts + 1, folder.getId());
    }
    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        repository.deleteById(existingId);
        Optional<Folder> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }
}
