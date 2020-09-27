package edu.bsu.sn.client.notepad.service;

import edu.bsu.sn.client.notepad.model.FileContent;
import edu.bsu.sn.client.security.service.SecurityService;
import edu.bsu.sn.client.web.client.SecuredNotepadClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author svku0919
 * @version 26.09.2020
 */

@Service
@RequiredArgsConstructor
public class NotepadService {
    private final SecurityService securityService;
    private final SecuredNotepadClient securedNotepadClient;

    public FileContent getFileContent(String fileName, String username) {
        FileContent fileContent = securedNotepadClient.getFileContent(fileName, username);
        String decryptedContent = securityService.decryptText(fileContent.getFileContent());
        return fileContent.setFileContent(decryptedContent);
    }
}
