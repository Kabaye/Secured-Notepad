package edu.bsu.sn.server.notepad.service;

import edu.bsu.sn.server.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotepadService {
    private final SecurityService securityService;

//    public String getFilesContent(String fileName){
//
//    }
}
