package br.com.DataPilots.Fileflow.tests;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.Folder;
import br.com.DataPilots.Fileflow.entities.User;

public class Factory {
    public static User createUser(String username, String password) {
        return new User(username, password);
    }

    public static File createFile() {
        File file = new File();
        file.setName("File");
        file.setId(1L);
        return file;
    }

    public static Folder createFolder() {
        Folder folder = new Folder();
        folder.setName("Folder");
        folder.setFolderId(1L);
        return folder;
    }
}
