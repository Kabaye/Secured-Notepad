package edu.bsu.sn.server.notepad.service;

import edu.bsu.sn.server.notepad.model.FileContent;
import edu.bsu.sn.server.notepad.model.UserFilesResponse;
import edu.bsu.sn.server.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotepadService {
    private final SecurityService securityService;

    @SneakyThrows
    public FileContent getFileContent(String fileName, String username) {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "initial/" + fileName);
        String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        return new FileContent()
                .setFileContent(securityService.encryptText(content, username))
                .setFileName(fileName)
                .setUsername(username);
    }

    @SneakyThrows
    public UserFilesResponse getUserFiles(String username) {
        final File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "texts/" + username);
        String files = Arrays.stream(file.listFiles())
                .map(File::getName)
                .collect(Collectors.joining(","));

        return new UserFilesResponse().setUserFiles(securityService.encryptText(files, username));
    }

    @SneakyThrows
    public boolean deleteUserFile(String fileName, String username) {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "texts/" + username +
                "/" + fileName);
        return file.delete();
    }

    @SneakyThrows
    public FileContent updateFileContent(FileContent fileContent) {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "texts/" +
                fileContent.getUsername() + "/" + fileContent.getFileName());
        Files.writeString(file.toPath(),
                securityService.decryptText(fileContent.getFileContent(), fileContent.getUsername()),
                StandardCharsets.UTF_8);

        return fileContent;
    }

    @SneakyThrows
    public FileContent addFile(String fileName, String username) {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "texts/" + username);
        Files.createFile(Path.of(file.getAbsolutePath() + "\\" + fileName));
        return new FileContent()
                .setFileName(fileName)
                .setUsername(username)
                .setFileContent("");
    }
}
