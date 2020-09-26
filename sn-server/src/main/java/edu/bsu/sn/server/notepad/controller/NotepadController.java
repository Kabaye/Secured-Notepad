package edu.bsu.sn.server.notepad.controller;

import edu.bsu.sn.server.notepad.model.FileContent;
import edu.bsu.sn.server.notepad.service.NotepadService;
import edu.bsu.sn.server.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notepad")
@RequiredArgsConstructor
public class NotepadController {
    private final NotepadService notepadService;

    public FileContent getFileContent(@RequestParam("file-name") String fileName, @RequestParam("username") String username){
        return notepadService.getFileContent(fileName, username);
    }
}
