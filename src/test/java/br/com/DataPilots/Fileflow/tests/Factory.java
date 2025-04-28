package br.com.DataPilots.Fileflow.tests;

import br.com.DataPilots.Fileflow.entities.File;
import br.com.DataPilots.Fileflow.entities.Folder;
import br.com.DataPilots.Fileflow.entities.User;
import com.github.javafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;

import java.sql.Timestamp;
import java.util.Date;

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

    public static User createFakeUser() {
        return Instancio.of(User.class)
            .set(Select.field(User::getId), null)
            .set(Select.field("username"), Faker.instance().internet().emailAddress())
            .set(Select.field("password"), Faker.instance().internet().password())
            .create();
    }

    public static Folder createFolder() {
        Folder folder = new Folder();
        folder.setName("Folder");
        folder.setUserId(1L);
        return folder;
    }

    public static File createFile() {
        Date randomDate = Faker.instance().date().past(10, java.util.concurrent.TimeUnit.DAYS);

        return Instancio.of(File.class)
            .set(Select.field(File::getId), null)
            .set(Select.field("name"), Faker.instance().file().fileName())
            .set(Select.field("mimeType"), Faker.instance().file().mimeType())
            .set(Select.field("base64"), Faker.instance().lorem().characters(120))
            .set(Select.field("size"), 100L)
            .set(Select.field("createdAt"), new Timestamp(randomDate.getTime()))
            .set(Select.field("userId"), null)
            .set(Select.field("folderId"), null)
            .create();
    }
}
