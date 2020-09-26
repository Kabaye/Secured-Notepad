package edu.bsu.sn.server.notepad.listener;

import edu.bsu.sn.server.notepad.model.event.UserLoggedIn;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ResourceUtils;

@Component
public class NotepadListener {
    @SneakyThrows
    public NotepadListener() {
        Path source = Path.of(ResourceUtils.getFile("classpath:initial/README.txt").getAbsolutePath());
        Path target = Path.of(source.toString().replace("initial\\README.txt",
                "texts"));
        if (Files.notExists(target)) {
            Files.createDirectory(target);
        }
    }

    @SneakyThrows
    @EventListener
    public void checkDirectoryForNewUser(UserLoggedIn userLoggedIn) {
        Path source = ResourceUtils.getFile("classpath:initial").toPath();
        Path targetDir = Path.of(ResourceUtils.getFile("classpath:texts").getAbsolutePath() + "\\" + userLoggedIn.getName());
        if (Files.notExists(targetDir)) {
            Files.createDirectory(targetDir);
            FileSystemUtils.copyRecursively(source, targetDir);
        }

    }
}
