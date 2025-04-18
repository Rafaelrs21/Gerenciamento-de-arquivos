package br.com.DataPilots.Fileflow.tests;

import br.com.DataPilots.Fileflow.entities.Folder;
import br.com.DataPilots.Fileflow.entities.User;

public class Factory {
    public static User createUser(String username, String password) {
        return new User(username, password);
    }

    public static Folder createFolder() {
        Folder folder = new Folder();
        folder.setName("Folder");
        folder.setUserId(1L);
        return folder;
    }
}
