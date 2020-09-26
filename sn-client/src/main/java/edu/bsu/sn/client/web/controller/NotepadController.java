package edu.bsu.sn.client.web.controller;

import edu.bsu.sn.client.web.client.SecuredNotepadClient;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotepadController {
    private final SecuredNotepadClient securedNotepadClient;

    @PostMapping
    public SecretKey createNewSessionKey() {
        return securedNotepadClient.askForNewSessionKey();
    }
}
