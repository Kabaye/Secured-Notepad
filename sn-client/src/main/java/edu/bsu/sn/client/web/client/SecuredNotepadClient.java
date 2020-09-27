package edu.bsu.sn.client.web.client;

import edu.bsu.sn.client.notepad.model.FileContent;
import edu.bsu.sn.client.notepad.model.UserFilesResponse;
import edu.bsu.sn.client.security.model.AESKeyAndIvSpec;
import edu.bsu.sn.client.security.model.LogInUser;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SecuredNotepadClient {
    private final RestTemplate restTemplate;

    public SecuredNotepadClient() {
        this.restTemplate = new RestTemplate();
    }

    @SneakyThrows
    public AESKeyAndIvSpec logIn(LogInUser logInUser) {
        return restTemplate.exchange("http://localhost:8280/api/v1/security/log-in", HttpMethod.POST,
                new HttpEntity<>(logInUser), AESKeyAndIvSpec.class)
                .getBody();
    }

    @SneakyThrows
    public FileContent getFileContent(String fileName, String username) {
        return restTemplate.exchange("http://localhost:8280" + UriComponentsBuilder.newInstance()
                .path("/api/v1/notepad/file")
                .queryParam("file-name", fileName)
                .queryParam("username", username)
                .toUriString(), HttpMethod.GET, null, FileContent.class)
                .getBody();
    }

    public UserFilesResponse getUserFiles(String username) {
        return restTemplate.exchange("http://localhost:8280" + UriComponentsBuilder.newInstance()
                .path("/api/v1/notepad/files")
                .queryParam("username", username)
                .toUriString(), HttpMethod.GET, null, UserFilesResponse.class)
                .getBody();
    }
}
