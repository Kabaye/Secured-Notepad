package edu.bsu.sn.server.security.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author svku0919
 * @version 27.09.2020
 */
@Data
@Accessors(chain = true)
public class AESKeyAndIvSpec {
    private byte[] aesKey;
    private byte[] ivSpec;
}
