package br.com.DataPilots.Fileflow.tests;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.Folder;
import br.com.DataPilots.Fileflow.entities.Group;
import br.com.DataPilots.Fileflow.entities.User;
import com.github.javafaker.Faker;

public class Factory {
    private static final Faker faker = new Faker();

    public static User createUser(String username, String password) {
        return new User(username, password);
    }

    public static User createUser() {
        return new User(faker.name().username(), faker.internet().password());
    }

    public static Folder createFolder() {
        Folder folder = new Folder();
        folder.setName("Folder");
        folder.setFolderId(1L);
        return folder;
    }

    public static File createFile() {
        File file = new File();
        file.setName(faker.file().fileName());
        return file;
    }

    public static File createFile(Long id) {
        File file = createFile();
        file.setId(id);
        return file;
    }

    public static Group createGroup() {
        Group group = new Group();
        group.setName(faker.company().name());
        return group;
    }

    public static Group createGroup(Long id) {
        Group group = createGroup();
        group.setId(id);
        return group;
    }
}
