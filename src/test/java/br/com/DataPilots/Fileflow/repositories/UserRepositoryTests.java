package br.com.DataPilots.Fileflow.repositories;

import br.com.DataPilots.Fileflow.entities.User;
import br.com.DataPilots.Fileflow.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UsersRepository repository;

    private Long existingId = 1L;
    private Long nonExistingId = 1000L;
    private Long countTotalProducts = 0L;

    @BeforeEach
    void setUp() throws Exception {
        repository.deleteAll();
        if (countTotalProducts != 0) {
            throw new IllegalStateException("DB não está vazio.");
        }

        User user = Factory.createUser("admin", "pass");
        repository.save(user);

        existingId = user.getId();
        nonExistingId = 1000L;

        countTotalProducts = repository.count();
    }

    @Test
    public void findByIdShouldReturnOptionalNonEmptyWhenIdExists(){
        Optional<User> result = repository.findById(existingId);
        System.out.println(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnOptionalEmptyWhenIdNonExists(){
        Optional<User> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        User user = Factory.createUser("user", "pass");
        user = repository.save(user);

        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(countTotalProducts + 1, user.getId());
    }
    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        repository.deleteById(existingId);
        Optional<User> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }
}
