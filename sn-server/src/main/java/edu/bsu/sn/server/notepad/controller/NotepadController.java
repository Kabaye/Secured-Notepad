package edu.bsu.sn.server.notepad.controller;

import edu.bsu.sn.server.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotepadController {
    private final SecurityService securityService;
}
