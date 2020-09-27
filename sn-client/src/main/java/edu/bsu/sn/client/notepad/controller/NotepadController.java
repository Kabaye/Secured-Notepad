package edu.bsu.sn.client.notepad.controller;

import edu.bsu.sn.client.notepad.model.FileContent;
import edu.bsu.sn.client.notepad.service.NotepadService;
import edu.bsu.sn.client.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.JOptionPane;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotepadController {
    private final NotepadService notepadService;
    private final SecurityService securityService;

    @GetMapping
    public FileContent getFileContent(@RequestParam("file-name") String fileName, @RequestParam("username") String username) {
        return notepadService.getFileContent(fileName, username);
    }

    @GetMapping("/test")
    public FileContent getFContent() {
        securityService.logIn("Kabaye");
//        return notepadService.getFileContent("README.txt", "Kabaye");
        final FileContent fileContent = notepadService.getFileContent("Родина - Михаил Лермонтов.txt", "Kabaye");
        JOptionPane.showMessageDialog(null, fileContent.getFileContent());
        return fileContent;
    }
}
