package edu.bsu.sn.server.notepad.model.event;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserLoggedIn {
    private String name;
}
