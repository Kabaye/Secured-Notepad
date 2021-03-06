package edu.bsu.sn.server.notepad.controller;

import edu.bsu.sn.server.notepad.model.FileContent;
import edu.bsu.sn.server.notepad.model.UserFilesResponse;
import edu.bsu.sn.server.notepad.service.NotepadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/notepad")
@RequiredArgsConstructor
public class NotepadController {
    private final NotepadService notepadService;

    @GetMapping("/file")
    public FileContent getFileContent(@RequestParam("file-name") String fileName, @RequestParam("username") String username) {
        fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        return notepadService.getFileContent(fileName, username);
    }

    @GetMapping("/files")
    public UserFilesResponse getUserFiles(@RequestParam("username") String username) {
        return notepadService.getUserFiles(username);
    }

    @DeleteMapping("/file")
    public boolean deleteFile(@RequestParam("file-name") String fileName, @RequestParam("username") String username) {
        fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        return notepadService.deleteUserFile(fileName, username);
    }

    @PutMapping("/file")
    public FileContent updateFileContent(@RequestBody FileContent fileContent) {
        return notepadService.updateFileContent(fileContent);
    }

    @PostMapping("/file")
    public FileContent addFile(@RequestParam("file-name") String fileName, @RequestParam("username") String username) {
        return notepadService.addFile(fileName, username);
    }
}
