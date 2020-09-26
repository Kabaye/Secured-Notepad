package edu.bsu.sn.client.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LogInUser {
    private byte[] publicKey;
    private String login;
}
