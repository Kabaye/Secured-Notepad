package edu.bsu.sn.server.security.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewUser {
    private byte[] publicKey;
    private String login;
}
