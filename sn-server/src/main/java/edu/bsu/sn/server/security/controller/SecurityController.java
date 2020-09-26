package edu.bsu.sn.server.security.controller;

import edu.bsu.sn.server.security.model.NewUser;
import edu.bsu.sn.server.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/security")
public class SecurityController {
    private final SecurityService securityService;

    @SneakyThrows
    @PostMapping("/register")
    public byte[] registerNewUser(@RequestBody NewUser newUser) {
        return securityService.registerNewUser(newUser);
    }
}
