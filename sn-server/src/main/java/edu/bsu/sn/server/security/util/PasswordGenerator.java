package edu.bsu.sn.server.security.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author svku0919
 * @version 30.09.2020
 */
@Component
public class PasswordGenerator {

    /* Assign a string that contains the set of characters you allow. */
    private static final byte[] symbols = "abcdefgjklmnprstuvwxyz0123456789".getBytes();
    private static final Integer PASSWORD_LENGTH = 7;

    private final Random random = new SecureRandom();

    public byte[] randomPassword() {
        final byte[] password = new byte[PASSWORD_LENGTH];
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password[i] = symbols[random.nextInt(symbols.length)];
        }
        return password;
    }

}
