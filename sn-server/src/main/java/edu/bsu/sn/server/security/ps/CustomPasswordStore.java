package edu.bsu.sn.server.security.ps;

import edu.bsu.sn.server.security.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author svku0919
 * @version 30.09.2020
 */
@Component
@RequiredArgsConstructor
public class CustomPasswordStore {
    private static final String PASSWORD_USER_ID_FORMAT = "user-%s-password";

    private final Map<String, byte[]> passwords = new ConcurrentHashMap<>();
    private final TextEncryptor textEncryptor;
    private final PasswordGenerator passwordGenerator;
    private File passwordFile;

    @PostConstruct
    @SneakyThrows
    public void loadPasswords() {
        passwordFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "security/passwords.txt");
        passwords.putAll(Files.lines(passwordFile.toPath())
                .filter(s -> !s.equals(""))
                .map(StringTokenizer::new)
                .collect(Collectors.toMap(stringTokenizer -> PASSWORD_USER_ID_FORMAT.formatted(stringTokenizer.nextToken()),
                        stringTokenizer -> stringTokenizer.nextToken().getBytes())));
    }

    public byte[] getPassword(String username) {
        return passwords.get(PASSWORD_USER_ID_FORMAT.formatted(username));
    }

    @SneakyThrows
    public void addPassword(String username, byte[] password) {
        String s = "\n" + username + " ";
        byte[] passEncrypted = textEncryptor.encrypt(new String(password, StandardCharsets.UTF_8)).getBytes();
        Files.write(passwordFile.toPath(), concat(s.getBytes(), passEncrypted), StandardOpenOption.APPEND);
        passwords.put(PASSWORD_USER_ID_FORMAT.formatted(username), passEncrypted);
    }

    public boolean checkPassword(String username, byte[] password) {
        return textEncryptor.decrypt(new String(passwords.get(PASSWORD_USER_ID_FORMAT.formatted(username)),
                StandardCharsets.UTF_8))
                .equals(new String(password, StandardCharsets.UTF_8));
    }

    public byte[] generateRandomPassword() {
        return passwordGenerator.randomPassword();
    }

    private byte[] concat(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;

        byte[] c = new byte[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }
}
